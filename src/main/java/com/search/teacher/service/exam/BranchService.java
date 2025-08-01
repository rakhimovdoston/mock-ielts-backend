package com.search.teacher.service.exam;

import com.search.teacher.dto.response.session.BranchResponse;
import com.search.teacher.dto.response.session.InfoResponse;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.Branch;
import com.search.teacher.model.entities.MockPackages;
import com.search.teacher.model.enums.TestTime;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.BranchRepository;
import com.search.teacher.repository.MockPackagesRepository;
import org.jetbrains.annotations.NotNull;
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

    public JResponse getAllBranches(boolean active) {
        List<Branch> branches = active ? branchRepository.findAllByActiveIsTrue() : branchRepository.findAll();
        List<BranchResponse> responses = getBranchResponses(branches);
        InfoResponse response = new InfoResponse();
        response.setBranches(responses);
        response.setPackages(mockPackagesRepository.findAllByActiveIsTrueOrderByOrdersAsc());
        response.setTestTimes(List.of(TestTime.morning.name(), TestTime.afternoon.name(), TestTime.evening.name()));

        return JResponse.success(response);
    }

    @NotNull
    private static List<BranchResponse> getBranchResponses(List<Branch> branches) {
        List<BranchResponse> responses = new ArrayList<>();
        for (Branch branch : branches) {
            BranchResponse response = new BranchResponse();
            response.setId(branch.getId());
            response.setActive(branch.isActive());
            response.setName(branch.getName());
            response.setDescription(branch.getDescription());
            response.setLocation(branch.getLocation());
            response.setMaxStudents(branch.getMaxStudents());
            responses.add(response);
        }
        return responses;
    }

    public JResponse getAllPackages() {
        List<MockPackages> mockPackages = mockPackagesRepository.findAllByActiveIsTrueOrderByOrdersAsc();
        return JResponse.success(mockPackages);
    }

    public JResponse activeBranch(Long id, boolean active) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new NotfoundException("Branch not found."));
        branch.setActive(active);
        branchRepository.save(branch);
        return JResponse.success();
    }
}
