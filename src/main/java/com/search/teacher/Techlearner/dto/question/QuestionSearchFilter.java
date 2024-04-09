package com.search.teacher.Techlearner.dto.question;

import com.search.teacher.Techlearner.dto.filter.PageFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionSearchFilter extends PageFilter {
    private String category;
    private String type;
}
