package com.search.teacher.Techlearner.dto.question;

import com.search.teacher.Techlearner.model.entities.Answer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDto {
    private Long id;

    private String name;

    private boolean correct;

    private boolean clientAnswer = false;
}
