package com.search.teacher.service.exam;

import com.search.teacher.dto.response.session.BranchResponse;
import com.search.teacher.dto.response.session.InfoResponse;
import com.search.teacher.model.entities.Branch;
import com.search.teacher.model.entities.MockPackages;
import com.search.teacher.model.enums.TestTime;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.BranchRepository;
import com.search.teacher.repository.MockPackagesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BranchService {
    private final BranchRepository branchRepository;
    private final MockPackagesRepository mockPackagesRepository;

    public BranchService(BranchRepository branchRepository, MockPackagesRepository mockPackagesRepository) {
        this.branchRepository = branchRepository;
        this.mockPackagesRepository = mockPackagesRepository;
    }

    public JResponse getAllBranches() {
        List<Branch> branches = branchRepository.findAllByActiveIsTrue();
        List<BranchResponse> responses = new ArrayList<>();
        for (Branch branch : branches) {
            BranchResponse response = new BranchResponse();
            response.setId(branch.getId());
            response.setName(branch.getName());
            response.setDescription(branch.getDescription());
            response.setLocation(branch.getLocation());
            response.setMaxStudents(branch.getMaxStudents());
            responses.add(response);
        }
        InfoResponse response = new InfoResponse();
        response.setBranches(responses);
        response.setPackages(mockPackagesRepository.findAllByActiveIsTrueOrderByOrdersAsc());
        response.setTestTimes(List.of(TestTime.morning.name(), TestTime.afternoon.name(), TestTime.evening.name()));

        return JResponse.success(response);
    }

    public JResponse getAllPackages() {
        List<MockPackages> mockPackages = mockPackagesRepository.findAllByActiveIsTrueOrderByOrdersAsc();
        return JResponse.success(mockPackages);
    }
}
