package com.search.teacher.repository;

import com.search.teacher.model.entities.MockTestExam;
import com.search.teacher.model.entities.UserWritingAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWritingAnswerRepository extends JpaRepository<UserWritingAnswer, Long> {

    boolean existsByMockTestExamAndWritingIdIn(MockTestExam mockTestExam, List<Long> writingId);
}
