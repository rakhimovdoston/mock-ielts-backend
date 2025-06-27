package com.search.teacher.repository;

import com.search.teacher.model.entities.ExamScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamScoreRepository extends JpaRepository<ExamScore, Long> {
}
