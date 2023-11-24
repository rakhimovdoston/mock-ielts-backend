package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
}
