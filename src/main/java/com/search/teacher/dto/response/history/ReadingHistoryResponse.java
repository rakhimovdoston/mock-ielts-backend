package com.search.teacher.dto.response.history;

import com.search.teacher.dto.response.module.ModuleAnswerResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReadingHistoryResponse {
    private List<ModuleAnswerResponse> answers = new ArrayList<>();
    private List<ModuleAnswerResponse> userAnswers = new ArrayList<>();
}
