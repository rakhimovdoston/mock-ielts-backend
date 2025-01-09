package com.search.teacher.dto.modules.listening;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswerDto {
    private Long questionId;
    private String answer;
}