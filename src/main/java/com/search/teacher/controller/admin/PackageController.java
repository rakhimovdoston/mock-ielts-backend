package com.search.teacher.controller.admin;

import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.exam.BranchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
