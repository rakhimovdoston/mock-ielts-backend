package com.search.teacher.repository;

import com.search.teacher.model.entities.MockTestExam;
import com.search.teacher.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockTestExamRepository extends JpaRepository<MockTestExam, Long> {

    MockTestExam findByActiveIsTrueAndUserAndStatus(User user, String status);

    Page<MockTestExam> findAllByUserOrderByCreatedDateDesc(User user, Pageable pageable);

    List<MockTestExam> findAllByUser(User user);

    MockTestExam findByIdAndUser(Long id, User user);

    MockTestExam findByExamUniqueIdAndUser(String examUniqueId, User user);

    MockTestExam findByExamUniqueId(String examUniqueId);

    MockTestExam findFirstByUser_IdOrderByCreatedDate(Long userId);
}
