package com.search.teacher.Techlearner.controller;

import com.search.teacher.Techlearner.dto.AuthenticationRequest;
import com.search.teacher.Techlearner.dto.UserDto;
import com.search.teacher.Techlearner.dto.request.ConfirmationRequest;
import com.search.teacher.Techlearner.dto.request.ForgotPasswordReq;
import com.search.teacher.Techlearner.dto.request.ResendRequest;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
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
        return ResponseEntity.ok(JResponse.success(userService.authenticate(request)));
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
