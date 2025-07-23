package com.search.teacher.service.exam;

import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.request.session.BookGroupRequest;
import com.search.teacher.dto.response.session.BookingResponse;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.*;
import com.search.teacher.model.enums.Status;
import com.search.teacher.model.projection.BookingProjection;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final TestSessionRepository testSessionRepository;
    private final BookingRepository bookingRepository;
    private final SpeakingSessionRepository speakingSessionRepository;
    private final MockPackagesRepository mockPackagesRepository;
    private final BookingGroupRepository bookingGroupRepository;
    private final SpeakingBookingRepository speakingBookingRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    @Transactional
    public JResponse setupBooking(User currentUser, BookGroupRequest request) {
        User user = userRepository.findById(request.userId()).orElseThrow(() -> new NotfoundException("Student Not found"));

        MockPackages mockPackages = mockPackagesRepository.findById(request.packageId())
                .orElseThrow(() -> new NotfoundException("Mock package not found"));

        Branch branch = branchRepository.findByIdAndActiveIsTrue(request.branchId());

        BookingGroup bookingGroup = new BookingGroup();
        bookingGroup.setMockPackages(mockPackages);
        bookingGroup.setUser(user);
        bookingGroup.setBranch(branch);
        bookingGroup.setCreatedByUser(currentUser.getId());
        bookingGroupRepository.save(bookingGroup);

        for (Long sessionId : request.sessionIds()) {
            TestSession session = testSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new NotfoundException("Test session not found"));

            if (bookingRepository.existsByTestSessionAndUserId(session, user.getId())) {
                return JResponse.error(400, "This is user already booked for this session");
            }

            int available = getAvailableSpace(session.getBranch(), session);
            if (available <= 0) {
                return JResponse.error(400, "This session is full. Please select another session.");
            }
            Booking booking = new Booking();
            booking.setUserId(user.getId());
            booking.setTestSession(session);
            booking.setTestTime(session.getTestTime());
            booking.setStatus(Status.CREATED.name());
            booking.setBranchId(session.getBranch().getId());
            booking.setBranchName(session.getBranch().getName());
            booking.setMainTestDate(session.getTestDate());
            booking.setBookingGroup(bookingGroup);
            bookingRepository.save(booking);
            bookingGroup.getBookings().add(booking);
        }

        for (Long sessionId : request.speakingSessionIds()) {
            SpeakingSession speakingSession = speakingSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new NotfoundException("Speaking session not found"));

            if (speakingBookingRepository.existsBySpeakingSession(speakingSession)) {
                return JResponse.error(400, "Speaking session already booked!");
            }

            SpeakingBooking booking = getSpeakingBooking(user, speakingSession, bookingGroup);
            speakingBookingRepository.save(booking);
            bookingGroup.getSpeakingBookings().add(booking);
        }

        bookingGroupRepository.save(bookingGroup);
        return JResponse.success();
    }

    @NotNull
    private SpeakingBooking getSpeakingBooking(User user, SpeakingSession speakingSession, BookingGroup bookingGroup) {
        SpeakingBooking booking = new SpeakingBooking();
        booking.setUserId(user.getId());
        booking.setStatus(Status.CREATED.name());
        booking.setMainTestDate(speakingSession.getDate());
        booking.setTestTime(speakingSession.getTime());
        booking.setSpeakingSession(speakingSession);
        booking.setBranchId(speakingSession.getBranch().getId());
        booking.setBranchName(speakingSession.getBranch().getName());
        booking.setSpeakingFullName(speakingSession.getBranch().getSpeakingFullName());
        booking.setBookingGroup(bookingGroup);
        return booking;
    }


    public int getAvailableSpace(Branch branch, TestSession session) {
        int booked = bookingRepository.countByTestSession(session);
        return branch.getMaxStudents() - booked;
    }

    public JResponse getAllBookingData(LocalDate date, String time, Long branchId, int page, int size, String type) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookingResponse> bookings = bookingRepository.findAllCombinedBookings(date, time, branchId, type, pageable);

        if (bookings.isEmpty()) {
            return JResponse.error(404, "No bookings found.");
        }
        PaginationResponse response = new PaginationResponse();
        response.setCurrentPage(page);
        response.setCurrentSize(size);
        response.setTotalPages(bookings.getTotalPages());
        response.setTotalSizes(bookings.getTotalElements());
        List<BookingResponse> responses = bookings.getContent();
        response.setData(responses);
        return JResponse.success(response);
    }

    private List<BookingResponse> toResponses(List<Booking> bookings) {
        List<BookingResponse> responses = new ArrayList<>();
        for (Booking booking : bookings) {
            responses.add(toDto(booking));
        }
        return responses;
    }

    public JResponse getBookingId(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotfoundException("Booking not found."));

        if (booking.getStatus().equals(Status.CREATED.name())) {
            booking.setStatus(Status.IN_COMPLETED.name());
            bookingRepository.save(booking);
        }
        return JResponse.success();
    }

    public Booking getUserExistBooking(User user) {
        String testTime = getCurrentTestShift();
//        Booking booking = bookingRepository.findFirstByUserIdAndMainTestDate(user.getId(), LocalDate.now());
        return bookingRepository.findFirstByUserIdAndMainTestDate(user.getId(), LocalDate.now());
    }

    public String getCurrentTestShift() {
        LocalTime now = LocalTime.now();

        if (isBetween(now, LocalTime.of(9, 50), LocalTime.of(10, 10))) {
            return "morning";
        } else if (isBetween(now, LocalTime.of(14, 20), LocalTime.of(14, 40))) {
            return "afternoon";
        } else if (isBetween(now, LocalTime.of(17, 20), LocalTime.of(17, 40))) {
            return "evening";
        } else {
            return "off-day";
        }
    }

    private boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {
        return !time.isBefore(start) && !time.isAfter(end);
    }

    private BookingResponse toDto(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        User user = booking.getBookingGroup().getUser();
        response.setStudentName(user.getFirstname() + " " + user.getLastname());
        response.setPhoneNumber(user.getPhone());
        response.setTime(booking.getTestTime());
        response.setEmail(user.getEmail());
        response.setStatus(booking.getStatus());
        response.setUsername(user.getUsername());
        response.setBranch(booking.getBranchName());
        response.setTestDate(booking.getMainTestDate());
        return response;
    }

    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public JResponse getAllBookingsByUser(User currentUser, Long userId) {
        List<BookingGroup> bookingGroups = bookingGroupRepository.findAllByUser_Id(userId);

        return JResponse.success();
    }
}
