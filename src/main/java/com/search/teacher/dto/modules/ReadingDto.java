package com.search.teacher.dto.modules;

import com.search.teacher.model.enums.Difficulty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Package com.search.teacher.dto.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 15:12
 **/
@Getter
@Setter
public class ReadingDto {
    private Long id;
    private Difficulty difficulty;
    @NotNull
    private String htmlText;
    @NotNull
    private String htmlQuestion;
    @NotNull
    private String htmlAnswer;

    private List<ModuleAnswerDto> answers = new ArrayList<>();
}
