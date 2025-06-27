package com.search.teacher.dto.request.module;

import jakarta.validation.constraints.NotNull;

public record WritingRequest(@NotNull String title, String image, boolean task) {
}
