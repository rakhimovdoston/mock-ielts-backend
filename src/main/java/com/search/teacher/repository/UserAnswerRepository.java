package com.search.teacher.repository;

import com.search.teacher.model.entities.UserAnswers;
import com.search.teacher.model.entities.UserExamAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswers, Long> {

    List<UserAnswers> findAllByUserExamAnswers(UserExamAnswers userExamAnswers);
}
