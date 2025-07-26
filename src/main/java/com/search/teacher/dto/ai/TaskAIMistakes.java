package com.search.teacher.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskAIMistakes {
    private String mistake;
    @JsonProperty("improved_version")
    private String improvedVersion;
}
