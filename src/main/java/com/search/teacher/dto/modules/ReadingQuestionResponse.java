package com.search.teacher.dto.modules;

import com.search.teacher.model.entities.modules.reading.Form;
import com.search.teacher.model.entities.modules.reading.QuestionTypes;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Package com.search.teacher.dto.modules
 * Created by doston.rakhimov
 * Date: 25/06/24
 * Time: 12:41
 **/
@Getter
@Setter
public class ReadingQuestionResponse {
    private Long id;
    private String text;
    private String count;
    private String explanation;
    private String condition;
    private String types;
    private List<Form> questions = new ArrayList<>();
    private List<RMultipleChoiceDto> choices = new ArrayList<>();
}
