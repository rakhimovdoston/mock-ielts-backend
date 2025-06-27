package com.search.teacher.repository;

import com.search.teacher.model.entities.QuestionTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionTypes, Long> {

    List<QuestionTypes> findAllByModuleTypeAndActiveIsTrue(String moduleType);

    QuestionTypes findByTypeAndModuleType(String type, String moduleType);

    QuestionTypes findByNameAndModuleType(String name, String moduleType);
}
