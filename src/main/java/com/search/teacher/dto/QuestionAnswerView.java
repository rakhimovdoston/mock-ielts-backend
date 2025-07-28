package com.search.teacher.dto;

import com.search.teacher.dto.ai.WritingAIFeedback;
import com.search.teacher.model.entities.UserWritingAnswer;
import com.search.teacher.model.entities.Writing;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionAnswerView {
    private Writing question;
    private UserWritingAnswer answer;
    private WritingAIFeedback feedback;
    private String score;
    private String summary;
}
