package com.search.teacher.dto.request;

public record ResetSectionRequest(String type, Long examId, String section, Long userId) {
}
