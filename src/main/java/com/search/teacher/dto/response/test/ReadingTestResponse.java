package com.search.teacher.dto.response.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.search.teacher.dto.response.module.QuestionResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReadingTestResponse {
    private Long id;
    private String type;
    private JsonNode content;
    List<QuestionResponse> questions = new ArrayList<>();
}
