package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Booking extends BaseEntity {

    private String status;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "test_session_id", referencedColumnName = "id")
    private TestSession testSession;

    @ManyToOne
    @JoinColumn(name = "bookking_group_id", referencedColumnName = "id")
    private BookingGroup bookingGroup;

    @OneToOne(mappedBy = "booking")
    public MockTestExam mockTestExam;

    private Long branchId;

    private String branchName;

    private LocalDate mainTestDate;

    private String testTime;
}
