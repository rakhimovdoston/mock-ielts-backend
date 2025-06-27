package com.search.teacher.dto.response.module;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadingPassageResponse {
    private Long id;
    private boolean active;
    private String title;
    private String type;
    private String error;
    private JsonNode content;
}
