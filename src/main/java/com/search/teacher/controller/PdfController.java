package com.search.teacher.controller;

import com.search.teacher.service.exam.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mock-exam-result/")
public class PdfController {

    private final ExamService examService;

    public PdfController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        return examService.downloadPdfFile(id);
    }
}
