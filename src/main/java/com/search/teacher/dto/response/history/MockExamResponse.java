package com.search.teacher.dto.response.history;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MockExamResponse {
    private Long id;
    private Date startDate;
    private Date endDate;
    private String reading;
    private String listening;
    private String writing;
    private String speaking;
    private String status;
}
