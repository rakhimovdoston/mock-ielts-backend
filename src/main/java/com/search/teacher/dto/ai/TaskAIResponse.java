package com.search.teacher.dto.ai;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TaskAIResponse {
    private Double score;
    private String reason;
    private List<TaskAIMistakes> mistakes = new ArrayList<>();
}
