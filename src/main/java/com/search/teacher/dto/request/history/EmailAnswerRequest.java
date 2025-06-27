package com.search.teacher.dto.request.history;

public record EmailAnswerRequest(
        Long userId,
        Long examId
) {
}
