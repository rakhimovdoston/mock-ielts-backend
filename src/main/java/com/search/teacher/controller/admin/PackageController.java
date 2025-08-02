package com.search.teacher.controller.admin;

import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.exam.BranchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/package")
public class PackageController {

    private final BranchService branchService;

    public PackageController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping("all")
    public JResponse getAllMockPackage() {
        return branchService.getAllPackages();
    }

    @GetMapping("active/{id}")
    public JResponse activeBranch(@PathVariable Long id, @RequestParam boolean active) {
        return branchService.activePackage(id, active);
    }
}
