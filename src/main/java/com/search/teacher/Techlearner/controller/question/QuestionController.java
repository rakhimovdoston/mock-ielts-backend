package com.search.teacher.Techlearner.controller.question;

import com.search.teacher.Techlearner.dto.question.QuestionDto;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<JResponse> getAllQuestion() {
        List<QuestionDto> questions = questionService.getAllQuestion();
        if (questions.isEmpty())
            return new ResponseEntity<>(JResponse.error(404, "Not found questions"), HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(JResponse.success(questions));
    }
}
