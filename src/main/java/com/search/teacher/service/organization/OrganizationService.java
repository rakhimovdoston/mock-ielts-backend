package com.search.teacher.service.organization;

import com.search.teacher.dto.UserDto;
import com.search.teacher.dto.filter.OrganizationFilter;
import com.search.teacher.dto.request.OrganizationRequest;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;

public interface OrganizationService {
    JResponse getAllOrganizations(OrganizationFilter filter);

    JResponse createOrganization(OrganizationRequest request);

    JResponse update(OrganizationRequest request);

    JResponse deleteOrganization(Long id);

    void createOrganisation(UserDto userDto, User user);

    Organization getOrganisationByOwner(User currentUser);
}
