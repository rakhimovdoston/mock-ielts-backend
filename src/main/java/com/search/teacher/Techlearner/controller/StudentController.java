package com.search.teacher.Techlearner.controller;

import com.search.teacher.Techlearner.dto.request.StudentRequest;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.dto.response.StudentResponse;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
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
