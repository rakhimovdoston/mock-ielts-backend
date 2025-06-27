package com.search.teacher.dto.response.test;

import com.search.teacher.dto.response.module.QuestionResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListeningTestResponse {
    private Long id;
    private String type;
    private String audio;
    private List<QuestionResponse> questions = new ArrayList<>();
}
