package com.search.teacher.Techlearner.controller.question;

import com.search.teacher.Techlearner.dto.filter.PaginationResponse;
import com.search.teacher.Techlearner.dto.question.AnswerList;
import com.search.teacher.Techlearner.dto.question.QuestionDto;
import com.search.teacher.Techlearner.dto.question.QuestionSearchFilter;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.question.QuestionService;
import com.search.teacher.Techlearner.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
@Tag(name = "Question Controller")
public class QuestionController {

    private final QuestionService questionService;
    private final SecurityUtils securityUtils;

    @GetMapping("get-categories")
    public JResponse getAllQuestionCategories() {
        return JResponse.success(questionService.getCategories());
    }

    @PostMapping("all")
    public ResponseEntity<JResponse> getAllQuestion(@RequestBody QuestionSearchFilter questionSearchFilter) {
        int count = questionService.getCountAllQuestion(questionSearchFilter);
        List<QuestionDto> questions = questionService.getAllQuestion(questionSearchFilter);
        if (questions == null || questions.isEmpty())
            return new ResponseEntity<>(JResponse.error(404, "Not found questions"), HttpStatus.NOT_FOUND);

        Collections.shuffle(questions);
        for (QuestionDto question: questions) {
            Collections.shuffle(question.getAnswers());
        }
        PaginationResponse response = new PaginationResponse();
        response.setCurrentPage(questionSearchFilter.getPage());
        response.setCurrentSize(questions.size());
        response.setTotalPages(count / questions.size());
        response.setTotalSizes(count);
        response.setData(questions);
        return ResponseEntity.ok(JResponse.success(response));
    }

    @PostMapping("check-answers")
    public ResponseEntity<JResponse> checkAnswers(@RequestBody AnswerList checker) {
        return ResponseEntity.ok(JResponse.success(questionService.checkAnswers(securityUtils.currentUser(), checker)));
    }
}
