package com.search.teacher.repository;

import com.search.teacher.model.entities.QuestionHistory;
import com.search.teacher.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface QuestionHistoryRepository extends JpaRepository<QuestionHistory, Long> {

    QuestionHistory findByUserAndRequestId(User user, String requestId);
    QuestionHistory findByRequestId(String requestId);
    List<QuestionHistory> findAllByUserAndDateBetween(User user, Date beginDate, Date endDate);
    List<QuestionHistory> findAllByDateBetween(Date beginDate, Date endDate);

    List<QuestionHistory> findAllByUser(User user);
}
