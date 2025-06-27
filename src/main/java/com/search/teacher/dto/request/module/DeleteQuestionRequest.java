package com.search.teacher.dto.request.module;

import java.util.List;

public record DeleteQuestionRequest(List<QuestionRequest> questions) {
}
