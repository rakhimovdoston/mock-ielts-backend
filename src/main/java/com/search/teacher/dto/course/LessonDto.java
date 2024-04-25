package com.search.teacher.dto.course;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonDto {
    private Long id;
    private String title;
    private String description;
    private String image;
}
