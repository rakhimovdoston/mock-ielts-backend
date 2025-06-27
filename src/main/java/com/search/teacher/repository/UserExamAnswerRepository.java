package com.search.teacher.repository;

import com.search.teacher.model.entities.MockTestExam;
import com.search.teacher.model.entities.UserExamAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExamAnswerRepository extends JpaRepository<UserExamAnswers, Long> {

    boolean existsByMockTestExamAndListeningIdIn(MockTestExam mockTestExam, List<Long> listeningId);

    boolean existsByMockTestExamAndReadingIdIn(MockTestExam mockTestExam, List<Long> readingId);
}
