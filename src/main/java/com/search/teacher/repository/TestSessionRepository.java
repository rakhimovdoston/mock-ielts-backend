package com.search.teacher.repository;

import com.search.teacher.model.entities.Branch;
import com.search.teacher.model.entities.TestSession;
import org.aspectj.weaver.ast.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TestSessionRepository extends JpaRepository<TestSession, Long> {

    List<TestSession> findAllByBranchAndTestDateGreaterThan(Branch branch, LocalDate date);

    List<TestSession> findAllByBranchAndTestDateBetween(Branch branch, LocalDate startDate, LocalDate endDate);

    List<TestSession> findAllByBranchAndTestDateBetweenAndTestTime(Branch branch, LocalDate startDate, LocalDate endDate, String testTime);

    List<TestSession> findAllByBranchAndTestDate(Branch branch, LocalDate date);

    List<TestSession> findAllByBranchAndTestDateAndTestTime(Branch branch, LocalDate date, String testTime);
}
