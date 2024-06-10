package com.search.teacher.service.organization;

import com.search.teacher.dto.request.OrganizationRequest;
import com.search.teacher.dto.filter.OrgFilter;
import com.search.teacher.model.response.JResponse;

public interface OrganizationService {
    JResponse getAllOrganizations(OrgFilter filter);

    JResponse createOrganization(OrganizationRequest request);

    JResponse update(OrganizationRequest request);

    JResponse deleteOrganization(Long id);
}
