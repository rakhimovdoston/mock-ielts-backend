package com.search.teacher.dto.response.session;

import com.search.teacher.model.enums.TestTime;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TestSessionView {
    private Long id;
    private LocalDate date;
    private TestTime time;
    private int existedSpace;
    private String branchName;
}
