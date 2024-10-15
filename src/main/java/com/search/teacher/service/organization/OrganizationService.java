package com.search.teacher.service.organization;

import com.search.teacher.dto.UserDto;
import com.search.teacher.dto.filter.OrganizationFilter;
import com.search.teacher.dto.request.OrganizationRequest;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;

public interface OrganizationService {
    JResponse getAllOrganizations(User currentUser, OrganizationFilter filter);

    JResponse createOrganization(User currentUser, OrganizationRequest request);

    JResponse update(User currentUser, OrganizationRequest request);

    JResponse deleteOrganization(User currentUser, Long id);

    Organization getOrganisationByOwner(User currentUser);
}
