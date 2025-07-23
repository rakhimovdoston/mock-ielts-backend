package com.search.teacher.dto.response.session;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SpeakingSessionView {
    private Long id;
    private LocalDate date;
    private String time;
    private String branchName;
    private String speakerName;
}