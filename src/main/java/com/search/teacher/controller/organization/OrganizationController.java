package com.search.teacher.controller.organization;

import com.search.teacher.dto.filter.OrganizationFilter;
import com.search.teacher.dto.request.OrganizationRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.organization.OrganizationService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "Organization")
@RequestMapping("api/v1/organization")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final SecurityUtils securityUtils;

    @PostMapping(value = "create")
    @ApiOperation(value = "New Organization create")
    public JResponse createOrganization(@RequestBody @Valid OrganizationRequest request) {
        return organizationService.createOrganization(securityUtils.getCurrentUser(), request);
    }

    @GetMapping("all")
    @Secured("ADMIN")
    @ApiOperation(value = "All Organization List Only for Admin")
    public JResponse getAll(@ParameterObject OrganizationFilter filter) {
        return organizationService.getAllOrganizations(securityUtils.getCurrentUser(), filter);
    }

    @PutMapping("update")
    @ApiOperation(value = "Update your organization")
    public JResponse update(@RequestBody @Valid OrganizationRequest request) {
        return organizationService.update(securityUtils.getCurrentUser(), request);
    }

    @DeleteMapping("delete")
    @Secured("ADMIN")
    @ApiOperation(value = "Delete Organization for only ADMIN")
    public JResponse delete(@RequestParam Long id) {
        return organizationService.deleteOrganization(securityUtils.getCurrentUser(), id);
    }
}
