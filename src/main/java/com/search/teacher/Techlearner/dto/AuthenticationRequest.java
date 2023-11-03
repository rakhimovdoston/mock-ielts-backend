package com.search.teacher.Techlearner.dto;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(
        @NotNull
        String email,
        @NotNull
        String password) {

}
