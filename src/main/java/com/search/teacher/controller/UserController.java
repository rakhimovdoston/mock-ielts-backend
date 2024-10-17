package com.search.teacher.controller;

import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.user.UserService;
import com.search.teacher.utils.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package com.search.teacher.controller
 * Created by doston.rakhimov
 * Date: 16/10/24
 * Time: 17:48
 **/

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final SecurityUtils securityUtils;
    private final UserService userService;

    public UserController(SecurityUtils securityUtils, UserService userService) {
        this.securityUtils = securityUtils;
        this.userService = userService;
    }

    @GetMapping("profile")
    public JResponse getProfileData() {
        return userService.getProfileData(securityUtils.getCurrentUser());
    }
}
