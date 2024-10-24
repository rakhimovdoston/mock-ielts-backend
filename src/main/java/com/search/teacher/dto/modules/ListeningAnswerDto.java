package com.search.teacher.dto.modules;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.search.teacher.model.entities.modules.reading.Form;

import java.util.List;

/**
 * Package com.search.teacher.dto.modules
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 15:19
 **/
public record ListeningAnswerDto(
        String instruction,
        String content,
        @JsonProperty("image_url")
        String imageUrl,
        String type,
        List<Form> questions
) {
}
