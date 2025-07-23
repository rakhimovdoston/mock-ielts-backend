package com.search.teacher.repository;

import com.search.teacher.model.entities.SpeakingBooking;
import com.search.teacher.model.entities.SpeakingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeakingBookingRepository extends JpaRepository<SpeakingBooking, Long> {
    boolean existsBySpeakingSession(SpeakingSession speakingSession);
}
