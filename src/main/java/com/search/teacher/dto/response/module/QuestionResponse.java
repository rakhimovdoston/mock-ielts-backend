package com.search.teacher.dto.response.module;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionResponse {
    private Long id;
    private String type;
    private JsonNode content;
    private int order;
}
