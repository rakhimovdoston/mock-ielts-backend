package com.search.teacher.dto.response.module;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WritingResponse {
    private Long id;
    private boolean active;
    private String title;
    private String image;
    private boolean task;
}
