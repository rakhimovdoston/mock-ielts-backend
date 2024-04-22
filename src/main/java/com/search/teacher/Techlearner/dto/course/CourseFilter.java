package com.search.teacher.Techlearner.dto.course;

import com.search.teacher.Techlearner.dto.filter.PageFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseFilter extends PageFilter {
    private String category;
}
