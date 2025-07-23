package com.search.teacher.dto.response.session;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchResponse {
    private Long id;
    private String name;
    private String description;
    private String location;
    private int maxStudents;
}
