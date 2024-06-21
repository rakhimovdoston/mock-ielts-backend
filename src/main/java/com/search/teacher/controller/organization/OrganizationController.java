package com.search.teacher.controller.organization;

import com.search.teacher.dto.filter.OrganizationFilter;
import com.search.teacher.dto.request.OrganizationRequest;
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

    @GetMapping("all")
    public JResponse getAll(@ParameterObject OrganizationFilter filter) {
        return service.getAllOrganizations(filter);
    }

    @PutMapping("update")
    public JResponse update(@RequestBody @Valid OrganizationRequest request) {
        return service.update(request);
    }

    @DeleteMapping("delete")
    public JResponse delete(@RequestParam Long id) {
        return service.deleteOrganization(id);
    }
}
