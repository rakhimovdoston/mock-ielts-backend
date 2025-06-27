package com.search.teacher.dto.response.module;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ModuleAnswerResponse {
    private Long key;
    private String value;
    private String keys;
    private List<String> values = new ArrayList<>();
}
