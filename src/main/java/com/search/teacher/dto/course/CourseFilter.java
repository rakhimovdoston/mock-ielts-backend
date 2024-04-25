package com.search.teacher.dto.course;

import com.search.teacher.dto.filter.PageFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseFilter extends PageFilter {
    private String category;
}
