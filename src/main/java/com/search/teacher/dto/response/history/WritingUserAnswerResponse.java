package com.search.teacher.dto.response.history;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WritingUserAnswerResponse {
    private Long id;
    private Long writingId;
    private String answer;
}
