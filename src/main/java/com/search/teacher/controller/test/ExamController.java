package com.search.teacher.controller.test;

import com.search.teacher.dto.request.test.TestUserAnswerRequest;
import com.search.teacher.dto.request.test.WritingTestRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.exam.ExamService;
import com.search.teacher.utils.SecurityUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/exam")
public class ExamController {

    private final ExamService examService;
    private final SecurityUtils securityUtils;

    public ExamController(ExamService examService, SecurityUtils securityUtils) {
        this.examService = examService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("start")
    public JResponse saveExam() {
        return examService.setExamsToUser(securityUtils.getCurrentUser());
    }

    @GetMapping("get/{id}")
    public JResponse getExam(@PathVariable String id) {
        return examService.getExam(securityUtils.getCurrentUser(), id);
    }

    @GetMapping("module/{id}")
    public JResponse getExamQuestionByModule(@PathVariable("id") Long id, @RequestParam(name = "moduleType") String moduleType) {

        if (moduleType.equals("reading") || moduleType.equals("writing") || moduleType.equals("listening"))
            return examService.getExamQuestionByModule(securityUtils.getCurrentUser(), id, moduleType);

        return JResponse.error(400, "Invalid type");
    }

    @PostMapping("answers/{id}")
    public JResponse saveReadingAnswers(@PathVariable("id") Long id, @RequestBody TestUserAnswerRequest request) {
        if (request.type().equals("reading") || request.type().equals("listening"))
            return examService.saveModuleAnswers(securityUtils.getCurrentUser(), id, request);

        return JResponse.error(400, "Invalid type");
    }

    @PostMapping("writing/{id}")
    public JResponse saveWritingAnswers(@PathVariable("id") Long id, @RequestBody WritingTestRequest request) {
        return examService.saveWritingModuleAnswer(securityUtils.getCurrentUser(), id, request);
    }
}
