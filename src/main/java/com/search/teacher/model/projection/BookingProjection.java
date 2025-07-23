package com.search.teacher.model.projection;

import java.time.LocalDate;

public interface BookingProjection {
    Long getId();
    Long getUserId();
    String getBranchName();
    String getStatus();
    String getTestTime();
    LocalDate getMainTestDate();
}
