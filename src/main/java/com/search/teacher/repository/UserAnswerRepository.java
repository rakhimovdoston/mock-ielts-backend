package com.search.teacher.repository;

import com.search.teacher.model.entities.UserAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswers, Long> {
}
