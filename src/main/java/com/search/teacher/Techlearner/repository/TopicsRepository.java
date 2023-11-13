package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Topics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicsRepository extends JpaRepository<Topics, Long> {
}
