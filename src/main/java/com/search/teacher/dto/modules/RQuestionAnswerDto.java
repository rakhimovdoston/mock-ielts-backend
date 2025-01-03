package com.search.teacher.dto.modules;

import com.search.teacher.model.entities.modules.reading.Form;
import com.search.teacher.model.entities.modules.reading.RMultipleChoice;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Package com.search.teacher.dto.modules
 * Created by doston.rakhimov
 * Date: 14/10/24
 * Time: 18:45
 **/
@Getter
@Setter
public class RQuestionAnswerDto {
    private Long id;
    private String content;
    private String instruction;
    private String type;
    private Integer questionCount;
    private List<Form> questions = new ArrayList<>();
    private List<RMultipleChoiceDto> choices = new ArrayList<>();
}
