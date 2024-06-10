package com.search.teacher.controller;

import com.search.teacher.dto.request.OrganizationRequest;
import com.search.teacher.dto.filter.OrgFilter;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.organization.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/organization")
public class OrganizationController {

    private final OrganizationService service;

    @PostMapping(value = "create")
    public JResponse createOrganization(@RequestBody @Valid OrganizationRequest request) {
        return service.createOrganization(request);
    }

    @GetMapping
    public JResponse getAll(@ParameterObject OrgFilter filter) {
        return service.getAllOrganizations(filter);
    }

    @PutMapping
    public JResponse update(@RequestBody @Valid OrganizationRequest request) {
        return service.update(request);
    }

    @DeleteMapping
    public JResponse delete(@RequestParam Long id) {
        return service.deleteOrganization(id);
    }


}
