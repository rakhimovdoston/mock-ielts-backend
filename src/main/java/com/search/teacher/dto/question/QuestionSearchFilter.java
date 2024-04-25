package com.search.teacher.dto.question;

import com.search.teacher.dto.filter.PageFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionSearchFilter extends PageFilter {
    private String category;
    private String type;
}
