package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.modules.reading.RMultipleChoice;
import com.search.teacher.model.entities.modules.reading.ReadingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Package com.search.teacher.repository.modules
 * Created by doston.rakhimov
 * Date: 25/10/24
 * Time: 15:26
 **/
@Repository
public interface RMultipleChoiceRepository extends JpaRepository<RMultipleChoice, Long> {

    List<RMultipleChoice> findAllByQuestion(ReadingQuestion question);

    @Query("SELECT r.correctAnswer FROM RMultipleChoice r WHERE r.question.id = :questionId")
    String findCorrectAnswerByQuestionId(@Param("questionId") Long questionId);

}

