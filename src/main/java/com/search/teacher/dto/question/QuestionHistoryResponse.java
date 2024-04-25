package com.search.teacher.dto.question;

import com.search.teacher.model.entities.QuestionHistory;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class QuestionHistoryResponse {
    private Long id;
    private String requestId;
    private Date date;
    private List<QuestionDto> questions;
    private int correctCount;

    public QuestionHistoryResponse(QuestionHistory questionHistory) {
        this.id = questionHistory.getId();
        this.requestId = questionHistory.getRequestId();
        this.date = questionHistory.getDate();
        this.correctCount = questionHistory.getCorrectCount();
    }
}
