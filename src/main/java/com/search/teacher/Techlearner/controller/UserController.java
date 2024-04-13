package com.search.teacher.Techlearner.controller;

import com.search.teacher.Techlearner.dto.response.StudentResponse;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.user.StudentService;
import com.search.teacher.Techlearner.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller")
public class UserController {
    private final StudentService studentService;
    private final SecurityUtils securityUtils;

    public UserController(StudentService studentService, SecurityUtils securityUtils) {
        this.studentService = studentService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("get")
    public JResponse getStudent() {
        StudentResponse response = studentService.getStudentById(securityUtils.currentUser());
        return JResponse.success(response);
    }
}
