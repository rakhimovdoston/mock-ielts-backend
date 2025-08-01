package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sms_info")
@Getter
@Setter
public class SmsInfo extends BaseEntity {
    private String phoneNumber;
    private String emailStatus;
    private String smsStatus;
    private String smsMessage;
    private Long mockTestId;
    private Long userId;
}
