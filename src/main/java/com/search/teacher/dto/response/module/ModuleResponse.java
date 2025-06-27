package com.search.teacher.dto.response.module;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModuleResponse {
    private List<QuestionResponse> questions;
    private List<ModuleAnswerResponse> answers;
}
