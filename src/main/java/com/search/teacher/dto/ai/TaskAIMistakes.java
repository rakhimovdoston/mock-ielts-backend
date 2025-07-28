package com.search.teacher.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskAIMistakes {
    @JsonProperty("mistake")
    private String mistake;
    @JsonProperty("explanation")
    private String explanation;
    @JsonProperty("improved_version")
    private String improvedVersion;
}
