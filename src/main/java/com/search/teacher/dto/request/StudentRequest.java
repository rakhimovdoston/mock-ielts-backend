package com.search.teacher.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StudentRequest {
    private Long id;
    private String email;

    private String firstname;
    private String lastname;
    private String phoneNumber;

    private Long describeId;
    private List<Long> topics = new ArrayList<>();
    private List<Long> goals = new ArrayList<>();
}
