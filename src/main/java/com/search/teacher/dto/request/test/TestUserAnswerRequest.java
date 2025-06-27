package com.search.teacher.dto.request.test;

import com.search.teacher.dto.request.module.ModuleAnswersRequest;

import java.util.List;

public record TestUserAnswerRequest(
        String type,
        List<ModuleAnswersRequest> questionAnswers
) {
}
