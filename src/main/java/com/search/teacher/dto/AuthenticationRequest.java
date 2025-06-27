package com.search.teacher.dto;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(
        @NotNull
        String username,
        @NotNull
        String password) {

}
