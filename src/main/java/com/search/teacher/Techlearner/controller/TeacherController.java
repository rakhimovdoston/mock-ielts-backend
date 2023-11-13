package com.search.teacher.Techlearner.controller;

import com.search.teacher.Techlearner.dto.request.TeacherRequest;
import com.search.teacher.Techlearner.dto.response.TeacherResponse;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.TeacherService;
import com.search.teacher.Techlearner.service.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final UserSession userSession;

    @GetMapping("degree")
    public JResponse getDegree() {
        return teacherService.allDegrees();
    }

    @PostMapping("new")
    public JResponse newTeacher(@RequestBody TeacherRequest request) {
        return JResponse.success(teacherService.newTeacher(userSession.getUser(), request));
    }

    @PostMapping("file")
    public ResponseEntity<JResponse> uploadProfilePhoto(@RequestPart MultipartFile file) {
        if (file.isEmpty())
            return ResponseEntity.badRequest().body(JResponse.error(400, "File is empty"));
        return ResponseEntity.ok(JResponse.success(teacherService.uploadFile(file)));
    }

    @GetMapping("profile/{id}")
    public JResponse profileData(@PathVariable("id") Long id) {
        TeacherResponse response = teacherService.getTeacher(id);
        return JResponse.success(response);
    }
}
