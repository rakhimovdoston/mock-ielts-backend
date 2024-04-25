package com.search.teacher.dto.question;

import com.search.teacher.model.entities.QCategory;
import com.search.teacher.model.enums.Difficulty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionDto {
    private Long id;
    private String name;
    private Difficulty difficulty;
    private QCategory category;
    private List<AnswerDto> answers = new ArrayList<>();
}
