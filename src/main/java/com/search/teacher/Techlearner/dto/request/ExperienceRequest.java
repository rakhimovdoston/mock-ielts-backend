package com.search.teacher.Techlearner.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExperienceRequest {
    private Long id;
    private String title;
    private String companyName;
    private String entry; // 2020-01-01
    private String end; // 2020-01-01
    private String description;
}
