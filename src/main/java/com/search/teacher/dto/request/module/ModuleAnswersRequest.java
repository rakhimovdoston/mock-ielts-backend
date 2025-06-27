package com.search.teacher.dto.request.module;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModuleAnswersRequest(
        @NotNull Long passageId,
        List<ModuleQuestionAnswerRequest> answers) {
}
