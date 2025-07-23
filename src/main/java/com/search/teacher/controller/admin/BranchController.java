package com.search.teacher.controller.admin;

import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.exam.BranchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/branch")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping("all")
    public JResponse getAllBranches() {
        return branchService.getAllBranches();
    }
}
