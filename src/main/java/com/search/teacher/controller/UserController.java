package com.search.teacher.controller;

import com.search.teacher.dto.UserDto;
import com.search.teacher.dto.request.UserUpdate;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.user.UserService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("User's data")
    public JResponse getProfileData() {
        return userService.getProfileData(securityUtils.getCurrentUser());
    }

    @PostMapping("update")
    @ApiOperation("User update him/her data")
    public JResponse updateProfile(@RequestBody @Valid UserUpdate userDto) {
        return  userService.updateProfileData(securityUtils.getCurrentUser(), userDto);
    }
 }
