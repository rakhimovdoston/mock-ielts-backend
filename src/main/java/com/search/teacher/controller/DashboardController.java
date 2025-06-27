package com.search.teacher.controller;

import com.search.teacher.dto.response.dashboard.DashboardResponse;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.module.ListeningService;
import com.search.teacher.service.module.ReadingService;
import com.search.teacher.service.module.WritingService;
import com.search.teacher.service.user.UserService;
import com.search.teacher.utils.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    private final UserService userService;
    private final ReadingService readingService;
    private final ListeningService listeningService;
    private final WritingService writingService;
    private final SecurityUtils securityUtils;

    public DashboardController(UserService userService, ReadingService readingService, ListeningService listeningService, WritingService writingService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.readingService = readingService;
        this.listeningService = listeningService;
        this.writingService = writingService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("all")
    public JResponse getAll() {
        int count = userService.countUser(securityUtils.getCurrentUser());
        int listening = listeningService.countListening(securityUtils.getCurrentUser());
        int reading = readingService.countReadings(securityUtils.getCurrentUser());
        int writing = writingService.countWritings(securityUtils.getCurrentUser());
        DashboardResponse response = new DashboardResponse();
        response.setTotalListening(listening);
        response.setTotalUsers(count);
        response.setTotalReading(reading);
        response.setTotalWriting(writing);
        return JResponse.success(response);
    }
}
