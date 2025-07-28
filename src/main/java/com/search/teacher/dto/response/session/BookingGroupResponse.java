package com.search.teacher.dto.response.session;

import com.search.teacher.dto.response.history.MockExamResponse;
import com.search.teacher.model.entities.MockPackages;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BookingGroupResponse {
    private Long id;
    private MockPackages mockPackages;
    private Date date;
    private List<MockExamResponse> results = new ArrayList<>();
}
