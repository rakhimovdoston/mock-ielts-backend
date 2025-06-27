package com.search.teacher.repository;

import com.search.teacher.model.entities.Listening;
import com.search.teacher.model.entities.ModuleAnswer;
import com.search.teacher.model.entities.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleAnswerRepository extends JpaRepository<ModuleAnswer, Long> {

    List<ModuleAnswer> findAllByReading(Reading reading);

    List<ModuleAnswer> findAllByListening(Listening listening);
}
