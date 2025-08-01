package com.search.teacher.repository;

import com.search.teacher.model.entities.SmsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsInfoRepository extends JpaRepository<SmsInfo, Long> {
    SmsInfo findByPhoneNumber(String phoneNumber);

    SmsInfo findByPhoneNumberAndMockTestId(String phoneNumber, Long mockTestId);
}
