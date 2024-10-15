package com.search.teacher.controller;

import com.search.teacher.dto.AuthenticationRequest;
import com.search.teacher.dto.UserDto;
import com.search.teacher.dto.request.ConfirmationRequest;
import com.search.teacher.dto.request.ForgotPasswordReq;
import com.search.teacher.dto.request.RefreshTokenReq;
import com.search.teacher.dto.request.ResendRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Controller")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<JResponse> register(@RequestBody @Valid UserDto userRequest) {
        JResponse response = userService.registerUser(userRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("authenticate")
    public ResponseEntity<JResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @PostMapping("refresh-token")
    public ResponseEntity<JResponse> refreshToken(@RequestBody RefreshTokenReq refreshTokenReq) {
        return ResponseEntity.ok(userService.refreshToken(refreshTokenReq));
    }

    @PostMapping("forgot-password")
    public ResponseEntity<JResponse> forgotPassword(@RequestBody ForgotPasswordReq request) {
        return ResponseEntity.ok(userService.forgotPassword(request));
    }

    @PostMapping("resend-code")
    public ResponseEntity<JResponse> resendEmail(@RequestBody @Valid ResendRequest request) {
        return ResponseEntity.ok(userService.resendEmailCode(request));
    }

    @PostMapping("confirmation-code")
    public ResponseEntity<JResponse> confirmationCode(@RequestBody @Valid ConfirmationRequest request) {
        JResponse response = userService.confirmationUser(request);
        return ResponseEntity.ok(response);
    }

}
