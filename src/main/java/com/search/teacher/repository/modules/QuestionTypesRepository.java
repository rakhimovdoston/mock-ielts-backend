package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.modules.QuestionTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Package com.search.teacher.repository.modules
 * Created by doston.rakhimov
 * Date: 22/06/24
 * Time: 18:32
 **/
@Repository
public interface QuestionTypesRepository extends JpaRepository<QuestionTypes, Long> {
}
