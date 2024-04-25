package com.search.teacher.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private String title;
    private Long teacherId;
}
