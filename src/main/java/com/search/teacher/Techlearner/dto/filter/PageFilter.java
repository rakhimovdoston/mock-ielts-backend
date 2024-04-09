package com.search.teacher.Techlearner.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageFilter {
    private int page = 0;
    private int size = 10;
}
