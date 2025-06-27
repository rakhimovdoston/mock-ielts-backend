package com.search.teacher.dto.request.module;

import com.fasterxml.jackson.databind.JsonNode;

public record QuestionRequest(
        Long id,
        String questionType,
        JsonNode questionContent,
        int order
) {
}
