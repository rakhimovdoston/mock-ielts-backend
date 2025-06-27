package com.search.teacher.controller;

import com.search.teacher.dto.AuthenticationRequest;
import com.search.teacher.dto.request.RefreshTokenReq;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "Authentication")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("authenticate")
    @ApiOperation("User can get access token")
    public ResponseEntity<JResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @PostMapping("refresh-token")
    @ApiOperation("User get new access token when old access token was expired")
    public ResponseEntity<JResponse> refreshToken(@RequestBody RefreshTokenReq refreshTokenReq) {
        return ResponseEntity.ok(userService.refreshToken(refreshTokenReq));
    }
}
