package com.search.teacher.controller.test;

import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.dto.request.history.EmailAnswerRequest;
import com.search.teacher.dto.request.history.ModuleScoreRequest;
import com.search.teacher.dto.request.history.ScoreRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.exam.ExamService;
import com.search.teacher.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/v1/history")
public class MockExamController {

    private final ExamService examService;
    private final SecurityUtils securityUtils;

    public MockExamController(ExamService examService, SecurityUtils securityUtils) {
        this.examService = examService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("all/{userId}")
    public JResponse getAllExam(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        UserFilter filter = new UserFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setFromDate(fromDate);
        filter.setToDate(toDate);
        return examService.allExams(securityUtils.getCurrentUser(), filter, userId);
    }

    @PostMapping("send-answer")
    public JResponse sendAnswer(@RequestBody EmailAnswerRequest request) {
        return examService.sendAnswerToEmail(securityUtils.getCurrentUser(), request);
    }

    @PostMapping("set-score/{userId}")
    public JResponse setScore(@PathVariable(name = "userId") Long userId, @RequestBody ScoreRequest request) {
        if (request.type().equals("writing") || request.type().equals("speaking")) {
            return examService.setScoreToUser(securityUtils.getCurrentUser(), userId, request);
        }

        return JResponse.error(400, "Invalid type");
    }

    @GetMapping("mock-exam/{byId}")
    public JResponse getMockExam(@PathVariable(name = "byId") Long id, @RequestParam(name = "type") String type) {
        if (type.equals("reading") || type.equals("listening") || type.equals("writing"))
            return examService.getMockExamHistory(securityUtils.getCurrentUser(), id, type);

        return JResponse.error(400, "Invalid type");
    }

    @PostMapping("score")
    public JResponse saveWriting(@RequestBody ModuleScoreRequest request) {
        if (request.moduleType().equals("reading") || request.moduleType().equals("writing") || request.moduleType().equals("listening"))
            return examService.giveScoreModule(securityUtils.getCurrentUser(), request);

        return JResponse.error(400, "Invalid type");
    }
}
