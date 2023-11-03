package com.search.teacher.Techlearner.dto.request;

import lombok.Getter;

@Getter
public class ConfirmationRequest {
    private String email;
    private String code;
    private boolean isForgotPassword;
}
