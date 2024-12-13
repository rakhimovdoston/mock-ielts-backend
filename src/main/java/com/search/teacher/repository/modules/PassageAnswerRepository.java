package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.modules.reading.PassageAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassageAnswerRepository extends JpaRepository<PassageAnswer, Long> {
}
