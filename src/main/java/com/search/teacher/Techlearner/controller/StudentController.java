package com.search.teacher.Techlearner.controller;

import com.search.teacher.Techlearner.dto.request.StudentRequest;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.dto.response.StudentResponse;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.user.StudentService;
import com.search.teacher.Techlearner.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {
    private final StudentService studentService;
    private final SecurityUtils securityUtils;

    public StudentController(StudentService studentService, SecurityUtils securityUtils) {
        this.studentService = studentService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("info")
    public JResponse getStudentData() {
        return studentService.getUserInfo(securityUtils.currentUser());
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
    public JResponse updateStudent(@RequestBody @Valid StudentRequest request) {
        SaveResponse response = studentService.updateStudent(request);
        return JResponse.success(response);
    }

    @GetMapping("/{id}")
    public JResponse getStudent(@PathVariable("id") Long id) {
        StudentResponse response = studentService.getStudentById(id);
        return JResponse.success(response);
    }
}
