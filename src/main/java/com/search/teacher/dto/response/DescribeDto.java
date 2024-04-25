package com.search.teacher.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DescribeDto {
    private Long id;
    private String name;
    private boolean active = false;
}
