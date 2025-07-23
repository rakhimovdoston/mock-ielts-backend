package com.search.teacher.repository.custom;

import com.search.teacher.dto.response.session.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CustomBookingRepository {
    Page<BookingResponse> findAllCombinedBookings(LocalDate date, String time, Long branchId, String type, Pageable pageable);
}
