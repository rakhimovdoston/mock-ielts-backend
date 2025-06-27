package com.search.teacher.dto.request.history;

public record ScoreRequest(Long examId, String type, String score, String description) {
}
