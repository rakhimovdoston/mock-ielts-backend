package com.search.teacher.repository;

import com.search.teacher.model.entities.Booking;
import com.search.teacher.model.entities.MockTestExam;
import com.search.teacher.model.entities.TestSession;
import com.search.teacher.repository.custom.CustomBookingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, CustomBookingRepository {

    int countByTestSession(TestSession testSession);

    boolean existsByTestSessionAndUserId(TestSession testSession, Long userId);

    Booking findByUserIdAndMainTestDateAndTestTime(Long userId, LocalDate mainTestDate, String testTime);

    Booking findFirstByUserIdAndMainTestDate(Long userId, LocalDate mainTestDate);

    Booking findByMockTestExam(MockTestExam mockTestExam);
}
