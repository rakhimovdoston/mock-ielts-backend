package com.search.teacher.controller;

import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.QuestionTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/question-type")
public class QuestionTypeController {

    private final QuestionTypeService questionTypeService;

    public QuestionTypeController(QuestionTypeService questionTypeService) {
        this.questionTypeService = questionTypeService;
    }

    @GetMapping("all")
    public JResponse getAllQuestionTypes(@RequestParam(required = false, defaultValue = "READING") String type) {
        return questionTypeService.getAllTypes(type);
    }
}
