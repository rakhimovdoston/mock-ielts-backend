package com.search.teacher.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WritingAIFeedback {

    @JsonProperty("task_response")
    private TaskAIResponse taskResponse;

    @JsonProperty("coherence_and_cohesion")
    private TaskAIResponse coherenceAndCohesion;

    @JsonProperty("lexical_resource")
    private TaskAIResponse lexicalResource;

    @JsonProperty("grammatical_range_and_accuracy")
    private TaskAIResponse grammaticalRangeAndAccuracy;

    @JsonProperty("task_achievement")
    private TaskAIResponse taskAchievement;

    @JsonProperty("overall_band")
    private Double overallScore;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("encouragement")
    private String encouragement;

    @JsonProperty("stickers")
    private List<String> stickers = new ArrayList<>();
}
