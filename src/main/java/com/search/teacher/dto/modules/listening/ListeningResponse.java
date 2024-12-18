package com.search.teacher.dto.modules.listening;

import com.search.teacher.dto.ImageDto;
import com.search.teacher.dto.modules.ReadingQuestionResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListeningResponse {
    private Long id;
    private String title;
    private String difficulty;
    private ImageDto audio;
    private int answerStart = 1;
    private List<ReadingQuestionResponse> question;
}
