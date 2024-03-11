package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Answer;
import com.search.teacher.Techlearner.model.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findAllByIdInAndQuestion(List<Long> ids, Question question);
}
