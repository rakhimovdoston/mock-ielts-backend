package com.search.teacher.dto.request.user;

import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotNull String username,
        @NotNull String password,
        String firstname,
        String lastname,
        String email,
        String phone) {
}
