package com.search.teacher.Techlearner.controller.test.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RandomUserResponse {
    private List<Person> results;
    private Info info;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    public boolean isError() {
        return results.isEmpty() && error != null;
    }
}
