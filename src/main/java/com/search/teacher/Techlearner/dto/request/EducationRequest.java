package com.search.teacher.Techlearner.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EducationRequest {
    private Long id;
    private String url;
    private String name; // university or school name
    private String degree;
    private String faculty; // optional
    private String entry; //    date(2018-09-01)
    private String end; // date(2022-07-01)
    private String description;
}
