package com.search.teacher.Techlearner.controller;

import com.search.teacher.Techlearner.dto.request.StudentRequest;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.dto.response.StudentResponse;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.user.StudentService;
import com.search.teacher.Techlearner.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/student")
@Tag(name = "Student Controller")
public class StudentController {
    private final StudentService studentService;
    private final SecurityUtils securityUtils;

    public StudentController(StudentService studentService, SecurityUtils securityUtils) {
        this.studentService = studentService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("info-desc")
    public JResponse infoDescription() {
        Map<String, Object> map = studentService.infoDescription();
        return JResponse.success(map);
    }

    @PostMapping("save")
    public JResponse saveStudent(@RequestBody @Valid StudentRequest requestBody) {
        return studentService.saveStudent(requestBody);
    }

    @PutMapping("update")
    public JResponse updateStudent(@RequestBody StudentRequest request) {
        SaveResponse response = studentService.updateStudent(securityUtils.currentUser(), request);
        return JResponse.success(response);
    }
}
