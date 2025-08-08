package com.search.teacher.repository;

import com.search.teacher.model.entities.SendAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SendAnswerRepository extends JpaRepository<SendAnswers, Long> {

    List<SendAnswers> findAllByStatusAndDate(String status, LocalDate date);

    SendAnswers findByExamUniqueIdAndUserId(String examUniqueId, Long userId);
}
