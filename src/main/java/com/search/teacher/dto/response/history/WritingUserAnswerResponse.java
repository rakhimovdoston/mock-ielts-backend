package com.search.teacher.dto.response.history;

import com.search.teacher.dto.ai.WritingAIFeedback;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WritingUserAnswerResponse {
    private Long id;
    private Long writingId;
    private String answer;
    private WritingAIFeedback feedback;
}
