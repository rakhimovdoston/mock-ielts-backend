package com.search.teacher.repository;

import com.search.teacher.model.entities.Branch;
import com.search.teacher.model.entities.SpeakingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SpeakingSessionRepository extends JpaRepository<SpeakingSession, Long> {
    List<SpeakingSession> findAllByBranchAndDateBetween(Branch branch, LocalDate today, LocalDate endDate);

    List<SpeakingSession> findAllByBranchAndDate(Branch branch, LocalDate date);
}
