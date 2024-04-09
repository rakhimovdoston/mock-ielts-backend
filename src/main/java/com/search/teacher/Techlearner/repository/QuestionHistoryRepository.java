package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.QuestionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionHistoryRepository extends JpaRepository<QuestionHistory, Long> {
}
