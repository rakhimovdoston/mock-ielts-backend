package com.search.teacher.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateFilter extends PageFilter {
    private String startDate;
    private String endDate;
}
