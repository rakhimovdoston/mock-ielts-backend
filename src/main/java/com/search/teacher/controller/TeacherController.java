//package com.search.teacher.controller;
//
//import com.search.teacher.dto.request.teacher.TeacherRequest;
//import com.search.teacher.dto.request.teacher.AddCertificate;
//import com.search.teacher.dto.response.TeacherResponse;
//import com.search.teacher.model.response.JResponse;
//import com.search.teacher.service.user.TeacherService;
//import com.search.teacher.utils.SecurityUtils;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/teachers/")
//@RequiredArgsConstructor
//@Tag(name = "Teacher Controller")
//public class TeacherController {
//
//    private final TeacherService teacherService;
//    private final SecurityUtils securityUtils;
//
//    @PostMapping("add-certificate")
//    public JResponse addCertificate(@RequestBody AddCertificate certificateRequest) {
//        return teacherService.addCertificate(securityUtils.getCurrentTeacher(), certificateRequest);
//    }
//
//    @DeleteMapping("delete-certificate/{certificateId}")
//    public JResponse deleteCertificate(@PathVariable Long certificateId) {
//        return teacherService.deleteCertificate(securityUtils.getCurrentTeacher(), certificateId);
//    }
//
//    @PostMapping("new")
//    public JResponse newTeacher(@RequestBody TeacherRequest request) {
//        return JResponse.success(teacherService.newTeacher(securityUtils.currentUser(), request));
//    }
//
//    @PostMapping("update")
//    public JResponse updateTeacher(@RequestBody TeacherRequest request) {
//        return JResponse.success(teacherService.updateTeacher(securityUtils.currentUser(), request));
//    }
//
//    @GetMapping("profile/data")
//    public JResponse profileData() {
//        TeacherResponse response = teacherService.getTeacher(securityUtils.currentUser());
//        return JResponse.success(response);
//    }
//}
