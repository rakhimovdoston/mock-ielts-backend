package com.search.teacher.dto.response;

import lombok.Getter;

@Getter
public class SaveResponse {
    private Long id;
    private String name;

    public SaveResponse(Long id) {
        this.id = id;
    }

    public SaveResponse(String name) {
        this.name = name;
    }

    public SaveResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
