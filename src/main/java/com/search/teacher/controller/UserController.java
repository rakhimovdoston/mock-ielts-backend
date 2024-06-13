package com.search.teacher.controller;

import com.search.teacher.dto.filter.MessageFilter;
import com.search.teacher.dto.filter.PageFilter;
import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.dto.response.StudentResponse;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.message.ChatMessageService;
import com.search.teacher.service.user.StudentService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller")
public class UserController {
    private final StudentService studentService;
    private final SecurityUtils securityUtils;
    private final ChatMessageService messageService;

    public UserController(StudentService studentService, SecurityUtils securityUtils, ChatMessageService messageService) {
        this.studentService = studentService;
        this.securityUtils = securityUtils;
        this.messageService = messageService;
    }

    @GetMapping("get")
    public JResponse getStudent() {
        StudentResponse response = studentService.getStudentById(securityUtils.currentUser());
        return JResponse.success(response);
    }

    @GetMapping
    public JResponse getUsersList(@ParameterObject UserFilter filter) {
        return studentService.getUsersList(filter);
    }

    @GetMapping("last-message")
    public JResponse getUserLastMessage(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                        @RequestParam(name = "toUserId") Long toUserId) {

        MessageFilter filter = new MessageFilter();
        filter.setPage(page);
        filter.setPage(size);
        filter.setToUserId(toUserId);
        return messageService.getLastMessage(securityUtils.currentUser(), filter);
    }

    @GetMapping("all-relative-users")
    public JResponse getUserAllConnection(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                          @RequestParam(name = "size", defaultValue = "20", required = false) int size) {
        PageFilter filter = new PageFilter();
        filter.setPage(page);
        filter.setSize(size);
        return messageService.getUserAllConnections(securityUtils.currentUser(), filter);
    }
}
