package com.search.teacher.Techlearner.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseDto {
    private Long id;

    @NonNull
    private String img;

    private boolean svg;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private Double price;

    private Double discount;

    @NonNull
    @JsonProperty("category_id")
    private Long categoryId;

    private List<LessonDto> lessons = new ArrayList<>();
}
