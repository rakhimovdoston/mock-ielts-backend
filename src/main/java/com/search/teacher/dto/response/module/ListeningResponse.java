package com.search.teacher.dto.response.module;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListeningResponse {
    private Long id;
    private boolean active;
    private String title;
    private String type;
    private String audio;
    private String error;
}
