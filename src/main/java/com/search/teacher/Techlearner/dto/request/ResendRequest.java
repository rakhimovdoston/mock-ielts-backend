package com.search.teacher.Techlearner.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendRequest {
    @NotNull
    private String email;
    private boolean isForgotPassword;
}
