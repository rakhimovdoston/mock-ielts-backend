package com.search.teacher.Techlearner.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class StudentRequest {
    private Long id;
    @NotNull
    private String email;

    private Long describeId;
    private List<Long> topics;
    private List<Long> goals;
}
