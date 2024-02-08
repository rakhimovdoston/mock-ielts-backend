package com.search.teacher.Techlearner.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmationRequest {
    private String email;
    private String code;
    private boolean isForgotPassword;
}
