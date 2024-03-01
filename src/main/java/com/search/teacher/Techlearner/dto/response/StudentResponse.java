package com.search.teacher.Techlearner.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentResponse {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private List<DescribeDto> describes;
    private List<DescribeDto> topics;
    private List<DescribeDto> goals;
    private String role;
}
