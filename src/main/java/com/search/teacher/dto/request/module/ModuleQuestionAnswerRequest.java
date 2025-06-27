package com.search.teacher.dto.request.module;

import java.util.List;

public record ModuleQuestionAnswerRequest(
        Long key,
        String value,
        String keys,
        List<String> values
) {
}
