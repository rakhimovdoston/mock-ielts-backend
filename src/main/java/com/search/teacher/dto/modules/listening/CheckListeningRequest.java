package com.search.teacher.dto.modules.listening;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckListeningRequest {
    private Long testId;
    private List<UserAnswerDto> answers;
}
