package com.search.teacher.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String content;
    private String userEmail;
    private String userProfileImage;
}
