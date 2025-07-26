package com.search.teacher.dto.response.session;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingHistory {
    private Long id;
    private String status;
    private String branch;
    private LocalDate testDate;
    private String time;
    private String type;
    private String speakerName;
}
