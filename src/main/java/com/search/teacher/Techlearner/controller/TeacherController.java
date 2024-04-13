package com.search.teacher.Techlearner.controller;

import com.search.teacher.Techlearner.dto.request.TeacherRequest;
import com.search.teacher.Techlearner.dto.response.TeacherResponse;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.user.TeacherService;
import com.search.teacher.Techlearner.service.UserSession;
import com.search.teacher.Techlearner.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("topic-info")
    public JResponse getTopics() {
        return teacherService.allTopics();
    }

    @PostMapping("new")
    public JResponse newTeacher(@RequestBody TeacherRequest request) {
        return JResponse.success(teacherService.newTeacher(securityUtils.currentUser(), request));
    }

    @PostMapping(value = "file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JResponse> uploadProfilePhoto(@RequestPart MultipartFile file) {
        if (file.isEmpty())
            return ResponseEntity.badRequest().body(JResponse.error(400, "File is empty"));

        return ResponseEntity.ok(JResponse.success(teacherService.uploadFile(securityUtils.currentUser(), file)));
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
