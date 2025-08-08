package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "send_answers")
@Getter
@Setter
public class SendAnswers extends BaseEntity {
    private LocalDate date;
    private String examUniqueId;
    private String emailResponse;
    private String smsResponse;
    private String status;
    private Long userId;

    public SendAnswers() {
    }

    public SendAnswers(LocalDate date, String examUniqueId, String emailResponse, String smsResponse, String status, Long userId) {
        this.date = date;
        this.examUniqueId = examUniqueId;
        this.emailResponse = emailResponse;
        this.smsResponse = smsResponse;
        this.status = status;
        this.userId = userId;
    }
}
