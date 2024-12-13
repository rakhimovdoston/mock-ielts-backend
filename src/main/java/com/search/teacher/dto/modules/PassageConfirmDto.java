package com.search.teacher.dto.modules;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PassageConfirmDto {
    private Long passageId;
    private List<PassageAnswerDto> answers = new ArrayList<>();
}
