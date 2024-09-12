package com.search.teacher.dto.modules;

import com.search.teacher.model.enums.Difficulty;
import lombok.Getter;
import lombok.Setter;

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
    private String passage;
    private Difficulty difficulty;
    private String title;
    private String explanation;
}
