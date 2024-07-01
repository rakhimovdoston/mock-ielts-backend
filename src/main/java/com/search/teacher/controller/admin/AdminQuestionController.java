package com.search.teacher.controller.admin;

import com.search.teacher.dto.question.QuestionDto;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.question.QuestionService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/question")
@RequiredArgsConstructor
@Tag(name = "Admin Question Controller")
public class AdminQuestionController {

    private final QuestionService questionService;
    private final SecurityUtils securityUtils;

    @PostMapping("save")
    public JResponse newQuestion(@RequestBody QuestionDto questionDto) {
        return questionService.saveQuestion(securityUtils.getCurrentUser(), questionDto);
    }

    @PostMapping("external")
    @Operation(summary = "call api from external api for")
    public JResponse newQuestionsFromExternal() {
        return questionService.externalQuestionRun();
    }

    @PutMapping("update/")
    public JResponse updateQuestion(@RequestBody QuestionDto questionDto) {
        return questionService.updateQuestion(securityUtils.getCurrentUser(), questionDto);
    }

    @PostMapping("excel")
    public ResponseEntity<JResponse> excelUploadQuestion(@RequestPart(name = "questions") MultipartFile questionFile) {
        if (questionFile.isEmpty())
            return new ResponseEntity<>(JResponse.error(400, "File is empty"), HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(questionService.uploadQuestions(securityUtils.getCurrentUser(), questionFile));
    }

    @DeleteMapping("delete/{id}")
    public JResponse deleteQuestion(@PathVariable("id") Long id) {
        return questionService.deleteQuestion(securityUtils.getCurrentUser(), id);
    }
}
