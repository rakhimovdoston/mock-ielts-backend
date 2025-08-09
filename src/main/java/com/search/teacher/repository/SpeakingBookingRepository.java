package com.search.teacher.repository;

import com.search.teacher.model.entities.BookingGroup;
import com.search.teacher.model.entities.SpeakingBooking;
import com.search.teacher.model.entities.SpeakingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeakingBookingRepository extends JpaRepository<SpeakingBooking, Long> {
    boolean existsBySpeakingSession(SpeakingSession speakingSession);

    List<SpeakingBooking> findAllByBookingGroup(BookingGroup bookingGroup);
}
