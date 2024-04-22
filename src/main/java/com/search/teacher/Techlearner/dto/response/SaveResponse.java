package com.search.teacher.Techlearner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SaveResponse {
    private Long id;
    private String name;

    public SaveResponse(Long id) {
        this.id = id;
    }

    public SaveResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
