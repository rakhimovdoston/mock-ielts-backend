package com.search.teacher.dto.request.test;

import java.util.List;

public record WritingTestRequest(
        List<WritingTestAnswerRequest> answers
) {
}
