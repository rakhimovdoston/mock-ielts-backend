package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking_groups")
@Getter
@Setter
public class BookingGroup extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "mock_packages_id", referencedColumnName = "id")
    private MockPackages mockPackages; // ONE_TIME, THREE_TIMES

    @OneToMany(mappedBy = "bookingGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "bookingGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    List<SpeakingBooking> speakingBookings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    private LocalDate createdLocaleDate;

    private Long createdByUser;
}
