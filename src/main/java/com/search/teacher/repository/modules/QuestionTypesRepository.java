package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.modules.reading.QuestionTypes;
import com.search.teacher.model.enums.ModuleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Package com.search.teacher.repository.modules
 * Created by doston.rakhimov
 * Date: 22/06/24
 * Time: 18:32
 **/
@Repository
public interface QuestionTypesRepository extends JpaRepository<QuestionTypes, Long> {

    List<QuestionTypes> findAllByModuleTypeAndActiveIsTrue(String moduleType);

    QuestionTypes findByActiveTrueAndModuleTypeAndName(String moduleType, String name);
}
