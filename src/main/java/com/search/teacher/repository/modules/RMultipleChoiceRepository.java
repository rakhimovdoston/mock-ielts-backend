package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.modules.reading.RMultipleChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Package com.search.teacher.repository.modules
 * Created by doston.rakhimov
 * Date: 25/10/24
 * Time: 15:26
 **/
@Repository
public interface RMultipleChoiceRepository extends JpaRepository<RMultipleChoice, Long> {
}
