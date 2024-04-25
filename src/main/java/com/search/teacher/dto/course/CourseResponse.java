package com.search.teacher.dto.course;

import com.search.teacher.dto.response.TeacherResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Double discount;
    private String category;
    private TeacherResponse teacher;
    private List<LessonDto> lessons = new ArrayList<>();
}
