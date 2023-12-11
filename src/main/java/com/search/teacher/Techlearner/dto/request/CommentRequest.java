package com.search.teacher.Techlearner.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private String title;
    private Long teacherId;
}
