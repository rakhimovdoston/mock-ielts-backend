package com.search.teacher.dto.response.dashboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardResponse {
    private int totalUsers;
    private int totalReading;
    private int totalWriting;
    private int totalListening;
}
