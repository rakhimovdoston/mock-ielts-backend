package com.search.teacher.dto.response.session;

import com.search.teacher.model.entities.MockPackages;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InfoResponse {
    private List<BranchResponse> branches = new ArrayList<>();
    private List<MockPackages> packages = new ArrayList<>();
    private List<String> testTimes = new ArrayList<>();
}
