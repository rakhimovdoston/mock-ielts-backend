package com.search.teacher.dto.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserFilter extends PageFilter {
    private String search;
    private Date fromDate;
    private Date toDate;
}
