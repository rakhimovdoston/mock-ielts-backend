package com.search.teacher.service.exam;

import com.search.teacher.model.response.JResponse;

import java.time.LocalDate;

public interface TestSessionService {
    JResponse getAvailableSessions(LocalDate date, String time, Long branchId);

    JResponse getAvailableSpeakingSessions(LocalDate date, Long branchId);
}
