package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Goals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalsRepository extends JpaRepository<Goals, Long> {
}
