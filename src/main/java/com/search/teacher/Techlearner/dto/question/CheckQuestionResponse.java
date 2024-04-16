package com.search.teacher.Techlearner.dto.question;

import com.search.teacher.Techlearner.model.entities.QuestionHistory;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CheckQuestionResponse {
    private List<QuestionDto> questions;
    private String requestId;
    private int countCorrect;
    private Date date;

    public CheckQuestionResponse(List<QuestionDto> questions, QuestionHistory questionHistory) {
        this.questions = questions;
        this.requestId = questionHistory.getRequestId();
        this.countCorrect = questionHistory.getCorrectCount();
    }

    public CheckQuestionResponse() {
    }
}
