package com.search.teacher.dto.modules;

/**
 * Package com.search.teacher.dto.modules
 * Created by doston.rakhimov
 * Date: 24/10/24
 * Time: 10:59
 **/
public record QuestionTypeDto(Long id,
                              String name,
                              String description,
                              String example,
                              String type) {
}
