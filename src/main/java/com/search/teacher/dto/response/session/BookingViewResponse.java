package com.search.teacher.dto.response.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.search.teacher.dto.message.UserResponse;
import com.search.teacher.dto.response.module.WritingResponse;
import com.search.teacher.dto.response.test.ListeningTestResponse;
import com.search.teacher.dto.response.test.ReadingTestResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BookingViewResponse {
    private String type;
    private BookingResponse booking;
    private List<ReadingTestResponse> readings = new ArrayList<>();
    private List<ListeningTestResponse> listening = new ArrayList<>();
    private List<WritingResponse> writings = new ArrayList<>();
    @JsonProperty("exam_id")
    private Long examId;
    private SpeakingSessionView speaking;
    private UserResponse user;
}
