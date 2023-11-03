package com.search.teacher.Techlearner.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendRequest {
    private String email;
    private boolean isForgotPassword;
}
