package com.search.teacher.Techlearner.dto.question;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionDto {
    private Long id;
    private String name;
    private List<AnswerDto> answers = new ArrayList<>();
}
