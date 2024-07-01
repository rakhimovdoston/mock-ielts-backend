package com.search.teacher.dto.modules;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Package com.search.teacher.dto.modules
 * Created by doston.rakhimov
 * Date: 25/06/24
 * Time: 12:40
 **/
@Getter
@Setter
public class ReadingResponse {
    private Long id;
    private String title;
    private String explanation;
    private String passage;
    private String content;
    private List<ReadingQuestionResponse> question;
}
