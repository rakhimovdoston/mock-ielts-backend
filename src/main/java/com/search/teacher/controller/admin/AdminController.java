package com.search.teacher.controller.admin;

import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.dto.request.RefreshAnswerRequest;
import com.search.teacher.dto.request.test.TestDateRequest;
import com.search.teacher.dto.request.user.UserRequest;
import com.search.teacher.dto.request.user.UsernameRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.exam.ExamService;
import com.search.teacher.service.exam.SendAnswerServices;
import com.search.teacher.service.user.UserService;
import com.search.teacher.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/v1/admin/user")
public class AdminController {

    private final UserService userService;
    private final SendAnswerServices services;
    private final SecurityUtils securityUtils;

    public AdminController(UserService userService, SendAnswerServices services, SecurityUtils securityUtils) {
        this.userService = userService;
        this.services = services;
        this.securityUtils = securityUtils;
    }

    @PostMapping("save")
    public JResponse saveUser(@RequestBody UserRequest request) {
        return userService.createUsers(securityUtils.getCurrentUser(), request);
    }

    @DeleteMapping("delete/{byId}")
    public JResponse deleteUser(@PathVariable(name = "byId") Long id) {
        return userService.deleteUsers(securityUtils.getCurrentUser(), id);
    }

    @PostMapping("create/teacher")
    public JResponse createTeacher(@RequestBody UserRequest request) {
        return userService.createTeacher(securityUtils.getCurrentUser(), request);
    }

    @PostMapping("check-username")
    public JResponse checkUsername(@RequestBody UsernameRequest request) {
        return userService.checkUsername(request.username()) ?
                JResponse.error(400, "Username is already taken") :
                JResponse.success();
    }

    @PutMapping("test-date/{byId}")
    public JResponse setStartTestDate(@PathVariable(name = "byId") Long id, @RequestBody TestDateRequest testDate) {
        return userService.setTestDate(securityUtils.getCurrentUser(), id, testDate);
    }

    @PutMapping("update/{byId}")
    public JResponse updateUser(@PathVariable(name = "byId") Long id, @RequestBody UserRequest request) {
        return userService.updateUser(securityUtils.getCurrentUser(), id, request);
    }

    @GetMapping("by/{id}")
    public JResponse getUserById(@PathVariable(name = "id") Long id) {
        return userService.getUserBydId(id);
    }

    @PostMapping("refresh-answers")
    public JResponse checkAllAnswers(@RequestBody RefreshAnswerRequest request) {
        return services.refreshAnswer(request);
    }

    @PostMapping("check-essay")
    public JResponse checkEssay(@RequestParam(name = "userId", required = false) Long userId) {
        return services.checkEssayAgainAndSendSms(userId);
    }

    @GetMapping("all")
    public JResponse getAllUsers(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(name = "search", required = false) String search
    ) {
        UserFilter userFilter = new UserFilter();
        userFilter.setFromDate(fromDate);
        userFilter.setToDate(toDate);
        userFilter.setPage(page);
        userFilter.setSize(size);
        userFilter.setSearch(search);
        return userService.allUsersByFilter(securityUtils.getCurrentUser(), userFilter);
    }
}
