package com.search.teacher.dto.response.history;

import com.search.teacher.dto.response.module.WritingResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WritingHistoryResponse {
    private List<WritingResponse> questions = new ArrayList<>();
    private List<WritingUserAnswerResponse> answers = new ArrayList<>();
    private String score;
}
