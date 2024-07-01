package com.search.teacher.controller;

import com.search.teacher.dto.request.StudentRequest;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.upload.FileService;
import com.search.teacher.service.user.StudentService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
@Tag(name = "Student Controller")
public class StudentController {
    private final StudentService studentService;
    private final SecurityUtils securityUtils;
    private final FileService fileService;

    public StudentController(StudentService studentService, SecurityUtils securityUtils, FileService fileService) {
        this.studentService = studentService;
        this.securityUtils = securityUtils;
        this.fileService = fileService;
    }

    @PostMapping("save")
    public JResponse saveStudent(@RequestBody @Valid StudentRequest requestBody) {
        return studentService.saveStudent(requestBody);
    }

    @PutMapping("update")
    public JResponse updateStudent(@RequestBody StudentRequest request) {
        SaveResponse response = studentService.updateStudent(securityUtils.getCurrentUser(), request);
        return JResponse.success(response);
    }
}
