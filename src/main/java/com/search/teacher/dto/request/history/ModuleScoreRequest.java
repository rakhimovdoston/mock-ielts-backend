package com.search.teacher.dto.request.history;

public record ModuleScoreRequest(
        String moduleType,
        Long examId,
        Long passageId,
        String score,
        String description
) {
}
