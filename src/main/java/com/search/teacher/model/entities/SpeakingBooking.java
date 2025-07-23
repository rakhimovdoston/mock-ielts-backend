package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "speaking_bookings")
@Getter
@Setter
public class SpeakingBooking extends BaseEntity {
    private String status;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "speaking_session_id", referencedColumnName = "id")
    private SpeakingSession speakingSession;

    @ManyToOne
    @JoinColumn(name = "booking_group_id", referencedColumnName = "id")
    private BookingGroup bookingGroup;

    private Long branchId;

    private String branchName;

    private LocalDate mainTestDate;

    private String testTime;

    private String speakingFullName;

}
