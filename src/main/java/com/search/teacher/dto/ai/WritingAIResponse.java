package com.search.teacher.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WritingAIResponse {
    @JsonProperty("task_1")
    private WritingAIFeedback taskOne;
    @JsonProperty("task_2")
    private WritingAIFeedback taskTwo;
}
