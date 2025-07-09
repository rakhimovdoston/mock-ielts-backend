package com.search.teacher.repository;

import com.search.teacher.model.entities.TestSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestSessionsRepository extends JpaRepository<TestSession, Long> {

    int countAllByUserId(Long userId);
}
