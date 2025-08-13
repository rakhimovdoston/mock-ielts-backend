package com.search.teacher.controller.admin;

import com.search.teacher.dto.request.session.BookGroupRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.exam.BookingService;
import com.search.teacher.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/booking")
public class BookingController {

    private final SecurityUtils securityUtils;
    private final BookingService bookingService;

    public BookingController(SecurityUtils securityUtils, BookingService bookingService) {
        this.securityUtils = securityUtils;
        this.bookingService = bookingService;
    }

    @PostMapping("set")
    public JResponse setBooking(@RequestBody BookGroupRequest request) {
        return bookingService.setupBooking(securityUtils.getCurrentUser(), request);
    }

    @GetMapping("all")
    public JResponse getAllBooking(@RequestParam(value = "date", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @RequestParam(value = "time", required = false) String time,
                                   @RequestParam(value = "branch", required = false) Long branchId,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                   @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        return bookingService.getAllBookingData(date, time, branchId, page, size, type);
    }

    @GetMapping("by-user")
    public JResponse getBookingsByUser(@RequestParam(value = "userId", required = false) Long userId) {
        return bookingService.getAllBookingsByUser(securityUtils.getCurrentUser(), userId);
    }

    @GetMapping("session/{id}/{type}")
    public JResponse getBookingId(@PathVariable Long id, @PathVariable String type) {
        return bookingService.getBookingId(id, type);
    }
}
