package com.search.teacher.dto.response.session;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private String studentName;
    private String phoneNumber;
    private String email;
    private String username;
    private String status;
    private String branch;
    private LocalDate testDate;
    private String time;
    private String tariff;
    private String type;
    private String speakerName;

    public BookingResponse(Long id, String studentName, String phoneNumber, String status, String branch, LocalDate testDate, String time, String type, String speakerName) {
        this.id = id;
        this.studentName = studentName;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.branch = branch;
        this.testDate = testDate;
        this.time = time;
        this.type = type;
        this.speakerName = speakerName;
    }
}
