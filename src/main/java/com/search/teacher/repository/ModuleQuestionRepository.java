package com.search.teacher.repository;

import com.search.teacher.model.entities.ModuleQuestions;
import com.search.teacher.model.entities.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleQuestionRepository extends JpaRepository<ModuleQuestions, Long> {

    Optional<ModuleQuestions> findByIdAndReading(Long id, Reading reading);
}
