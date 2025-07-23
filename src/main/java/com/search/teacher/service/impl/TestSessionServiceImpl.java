package com.search.teacher.service.impl;

import com.search.teacher.dto.response.session.SpeakingSessionView;
import com.search.teacher.dto.response.session.TestSessionView;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.Branch;
import com.search.teacher.model.entities.SpeakingSession;
import com.search.teacher.model.entities.TestSession;
import com.search.teacher.model.enums.TestTime;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.*;
import com.search.teacher.service.exam.TestSessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestSessionServiceImpl implements TestSessionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TestSessionRepository testSessionRepository;
    private final BookingRepository bookingRepository;
    private final BranchRepository branchRepository;
    private final SpeakingSessionRepository speakingSessionRepository;
    private final SpeakingBookingRepository speakingBookingRepository;

    @Override
    public JResponse getAvailableSessions(LocalDate date, String time, Long branchId) {
        Branch branch = getBranchById(branchId);
        LocalDate today = LocalDate.now();

        if (date.isBefore(today)) {
            List<TestSession> sessions = !time.equals("all") ?
                    testSessionRepository.findAllByBranchAndTestDateAndTestTime(branch, date, time) :
                    testSessionRepository.findAllByBranchAndTestDate(branch, date);
            if (sessions.isEmpty()) {
                return JResponse.error(404, "Test session not found for date: " + date + ". Please select another date.");
            }
            return JResponse.success(convertToView(sessions, branch));
        }

        LocalDate endDate;
        if (date.isEqual(today)) {
            endDate = today.plusDays(6);
        } else {
            endDate = date;
        }

        List<TestSession> sessions = !time.equals("all") ?
                testSessionRepository.findAllByBranchAndTestDateBetweenAndTestTime(branch, today, endDate, time) :
                testSessionRepository.findAllByBranchAndTestDateBetween(branch, today, endDate);

        Set<LocalDate> existingDates = sessions.stream()
                .map(TestSession::getTestDate)
                .collect(Collectors.toSet());

        List<TestSession> toSave = new ArrayList<>();

        for (LocalDate dateToCheck = today; !dateToCheck.isAfter(endDate); dateToCheck = dateToCheck.plusDays(1)) {
            if (!existingDates.contains(dateToCheck)) {
                for (TestTime testTime : TestTime.values()) {
                    TestSession session = new TestSession();
                    session.setBranch(branch);
                    session.setTestTime(testTime.name());
                    session.setTestDate(dateToCheck);
                    toSave.add(session);
                }
            }
        }

        if (!toSave.isEmpty()) {
            List<TestSession> saved = testSessionRepository.saveAll(toSave);
            if (time.equals("all")) {
                sessions.addAll(saved);
            } else {
                sessions.addAll(saved.stream().filter(session -> session.getTestTime().equals(time)).toList());
            }
        }

        return JResponse.success(convertToView(sessions, branch));
    }

    private List<TestSessionView> convertToView(List<TestSession> sessions, Branch branch) {
        List<TestSessionView> views = new ArrayList<>();
        for (TestSession session : sessions) {
            TestSessionView view = new TestSessionView();
            view.setId(session.getId());
            view.setTime(TestTime.getValue(session.getTestTime()));
            view.setDate(session.getTestDate());
            view.setBranchName(branch.getName());
            view.setExistedSpace(getExistedSpace(branch, session));
            views.add(view);
        }
        views.sort(Comparator.comparing(TestSessionView::getDate).thenComparing(TestSessionView::getTime));
        return views;
    }

    @Override
    public JResponse getAvailableSpeakingSessions(LocalDate date, Long branchId) {
        if (date == null) {
            date = LocalDate.now();
        }

        Branch branch = getBranchById(branchId);
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(21, 0);

        // Mavjud sessiyalarni olib kelamiz
        List<SpeakingSession> existingSessions = speakingSessionRepository.findAllByBranchAndDate(branch, date);
        Map<String, SpeakingSession> existingMap = existingSessions.stream()
                .collect(Collectors.toMap(SpeakingSession::getTime, Function.identity(), (a, b) -> a));

        List<SpeakingSession> toCreate = new ArrayList<>();
        LocalTime current = start;

        while (current.isBefore(end)) {
            String timeStr = current.toString();

            if (!existingMap.containsKey(timeStr)) {
                SpeakingSession session = new SpeakingSession();
                session.setDate(date);
                session.setTime(timeStr);
                session.setBranch(branch);
                session.setSpeakingFullName(branch.getSpeakingFullName());
                toCreate.add(session);
            }

            current = current.plusMinutes(20);
        }

        // Yangi sessiyalarni saqlaymiz va map ga qo‘shamiz
        List<SpeakingSession> saved = speakingSessionRepository.saveAll(toCreate);
        saved.forEach(session -> existingMap.put(session.getTime(), session));

        // Faqat bo‘sh sessiyalarni yig‘amiz
        List<SpeakingSessionView> availableViews = existingMap.values().stream()
                .filter(session -> !speakingBookingRepository.existsBySpeakingSession(session))
                .map(session -> {
                    SpeakingSessionView view = new SpeakingSessionView();
                    view.setId(session.getId());
                    view.setDate(session.getDate());
                    view.setTime(session.getTime());
                    view.setBranchName(branch.getName());
                    view.setSpeakerName(session.getSpeakingFullName());
                    return view;
                })
                .sorted(Comparator.comparing(SpeakingSessionView::getTime))
                .collect(Collectors.toList());

        if (availableViews.isEmpty()) {
            return JResponse.error(404, "No available speaking sessions on " + date + ". Please select another time.");
        }

        return JResponse.success(availableViews);
    }

    private int getExistedSpace(Branch branch, TestSession testSession) {
        int count = bookingRepository.countByTestSession(testSession);
        return Math.max(branch.getMaxStudents() - count, 0);
    }

    private Branch getBranchById(Long branchId) {
        return branchRepository.findById(branchId).orElseThrow(() -> new NotfoundException("Branch not found"));
    }
}
