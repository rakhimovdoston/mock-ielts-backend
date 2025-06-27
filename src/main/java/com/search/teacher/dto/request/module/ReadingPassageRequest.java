package com.search.teacher.dto.request.module;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;

public record ReadingPassageRequest(
        @NotNull String title,
        @NotNull String type,
        @NotNull JsonNode content) {
}
