package com.search.teacher.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TaskAIResponse {
    @JsonProperty("score")
    private Double score;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("suggestion")
    private String suggestion;

    @JsonProperty("strength")
    private String strength;
    @JsonProperty("sticker")
    private String sticker;

    @JsonProperty("mistakes")
    private List<TaskAIMistakes> mistakes = new ArrayList<>();
}
