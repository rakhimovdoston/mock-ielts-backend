package com.search.teacher.dto.modules;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PassageAnswerDto {
    private Long id;
    private String text;
    private String count;
    private List<String> answers = new ArrayList<>();
}
