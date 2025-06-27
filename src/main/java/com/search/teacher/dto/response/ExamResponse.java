package com.search.teacher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponse {
    private Long id;
    private boolean listening;
    private boolean reading;
    private boolean writing;
    private Long leftDuration;
}
