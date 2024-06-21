package com.search.teacher.dto.modules;

import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.dto.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 15:33
 **/
@Getter
@Setter
public class ModuleAnswerDto {
    private Integer questionId;
    private String answer;
}
