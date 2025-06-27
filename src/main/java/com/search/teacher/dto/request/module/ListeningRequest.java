package com.search.teacher.dto.request.module;

import jakarta.validation.constraints.NotNull;

public record ListeningRequest(
        @NotNull String title,
        @NotNull String audio,
        @NotNull String type
) {
}
