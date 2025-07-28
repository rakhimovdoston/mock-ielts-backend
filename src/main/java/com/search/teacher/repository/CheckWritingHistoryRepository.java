package com.search.teacher.repository;

import com.search.teacher.model.entities.CheckWritingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckWritingHistoryRepository extends JpaRepository<CheckWritingHistory, Long> {
}
