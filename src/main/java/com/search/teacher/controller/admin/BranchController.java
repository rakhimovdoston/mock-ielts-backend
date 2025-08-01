package com.search.teacher.controller.admin;

import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.exam.BranchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/branch")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping("all")
    public JResponse getAllBranches(@RequestParam(required = false, defaultValue = "true") boolean active) {
        return branchService.getAllBranches(active);
    }

    @GetMapping("active/{id}")
    public JResponse activeBranch(@PathVariable Long id, @RequestParam boolean active) {
        return branchService.activeBranch(id, active);
    }
}
