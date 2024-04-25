package com.search.teacher.Techlearner.controller;

import com.search.teacher.Techlearner.dto.request.teacher.TeacherRequest;
import com.search.teacher.Techlearner.dto.request.teacher.AddCertificate;
import com.search.teacher.Techlearner.dto.response.TeacherResponse;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.user.TeacherService;
import com.search.teacher.Techlearner.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
@Tag(name = "Teacher Controller")
public class TeacherController {

    private final TeacherService teacherService;
    private final SecurityUtils securityUtils;

    @GetMapping("degree")
    public JResponse getDegree() {
        return teacherService.allDegrees();
    }

    @PostMapping("add-certificate")
    public JResponse addCertificate(@RequestBody AddCertificate certificateRequest) {
        return teacherService.addCertificate(securityUtils.getCurrentTeacher(), certificateRequest);
    }

    @GetMapping("topic-info")
    public JResponse getTopics() {
        return teacherService.allTopics();
    }

    @PostMapping("new")
    public JResponse newTeacher(@RequestBody TeacherRequest request) {
        return JResponse.success(teacherService.newTeacher(securityUtils.currentUser(), request));
    }

    @PostMapping("update")
    public JResponse updateTeacher(@RequestBody TeacherRequest request) {
        return JResponse.success(teacherService.updateTeacher(securityUtils.currentUser(), request));
    }

    @GetMapping("profile/data")
    public JResponse profileData() {
        TeacherResponse response = teacherService.getTeacher(securityUtils.currentUser());
        return JResponse.success(response);
    }
}
