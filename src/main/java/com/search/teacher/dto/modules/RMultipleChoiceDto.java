package com.search.teacher.dto.modules;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Package com.search.teacher.dto.modules
 * Created by doston.rakhimov
 * Date: 25/10/24
 * Time: 13:58
 **/
@Getter
@Setter
public class RMultipleChoiceDto {
    private Long id;
    private String name;
    private int order;
    private List<String> answers;
    private String correctAnswer;
}
