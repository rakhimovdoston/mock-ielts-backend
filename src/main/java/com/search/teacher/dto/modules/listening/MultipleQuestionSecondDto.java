package com.search.teacher.dto.modules.listening;

import com.search.teacher.model.entities.modules.reading.Form;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MultipleQuestionSecondDto {
    private String conditions;
    private String questionCount;
    private List<Form> forms = new ArrayList<>();
}
