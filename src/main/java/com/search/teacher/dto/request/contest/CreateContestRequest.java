package com.search.teacher.dto.request.contest;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateContestRequest {
    private String name;
    private String description;
    private String startTime;
    private List<Long> readingIds = new ArrayList<>();
    private List<Long> listeningIds = new ArrayList<>();
    private List<Long> writingIds = new ArrayList<>();
}
