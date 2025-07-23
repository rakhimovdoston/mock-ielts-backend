package com.search.teacher.controller.admin;

import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.exam.TestSessionService;
import com.search.teacher.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/test-session")
public class TestSessionController {

    private final SecurityUtils securityUtils;
    private final TestSessionService testSessionService;

    public TestSessionController(SecurityUtils securityUtils, TestSessionService testSessionService) {
        this.securityUtils = securityUtils;
        this.testSessionService = testSessionService;
    }

    @GetMapping("available")
    public JResponse getAvailableTestSessions(
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "time", required = false) String time,
            @RequestParam(name = "branch") Long branchId) {
        if (date == null)
            date = LocalDate.now();
        return testSessionService.getAvailableSessions(date, time, branchId);
    }

    @GetMapping("speaking/available")
    public JResponse getAvailableSpeakingSession(
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "branch") Long branchId) {
        if (date == null)
            date = LocalDate.now();
        return testSessionService.getAvailableSpeakingSessions(date, branchId);
    }
}
