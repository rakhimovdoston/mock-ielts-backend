package com.search.teacher.service.impl;

import com.search.teacher.dto.UserDto;
import com.search.teacher.dto.filter.OrganizationFilter;
import com.search.teacher.dto.request.OrganizationRequest;
import com.search.teacher.dto.response.OrganizationResponse;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.OrganizationRepository;
import com.search.teacher.service.organization.OrganizationService;
import com.search.teacher.utils.SecurityUtils;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public JResponse getAllOrganizations(User currentUser, OrganizationFilter filter) {
        Page<Organization> orgPaged = organizationRepository.findAllByFilter(filter);

        List<OrganizationResponse> responseData = orgPaged.getContent().stream().map(organization -> OrganizationResponse.builder()
                .name(organization.getName())
                .description(organization.getDescription())
                .address(organization.getAddress())
                .city(organization.getCity())
                .phoneNumber(organization.getPhoneNumber())
                .email(organization.getEmail())
                .website(organization.getWebsite())
                .owner(getOwnerName(organization.getOwner()))
                .build()).toList();

        if (Collections.isEmpty(responseData)) return JResponse.error(404, "Organisations not found");

        return JResponse.success(responseData);
    }

    private String getOwnerName(User owner) {
        return owner.getFirstname() + " " + owner.getLastname();
    }

    @Transactional
    @Override
    public JResponse createOrganization(User currentUser, OrganizationRequest request) {
        Organization organization = organizationRepository.findByOwner(currentUser);
        if (organization != null)
            throw new BadRequestException("Organization already exists");

        organization = Organization
                .builder()
                .name(request.name())
                .address(request.address())
                .city(request.city())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .website(request.website())
                .owner(currentUser)
                .build();

        organizationRepository.save(organization);
        return JResponse.success(new SaveResponse(organization.getId()));
    }

    @Transactional
    @Override
    public JResponse update(User currentUser, OrganizationRequest request) {
        Organization existingOrganization = organizationRepository.findById(request.id())
                .orElseThrow(() -> new NotfoundException("Organization not found with id " + request.id()));

        if (!existingOrganization.getOwner().equals(currentUser)) {
            throw new NotfoundException("Organization not found with id " + request.id());
        }

        existingOrganization.setName(request.name());
        existingOrganization.setAddress(request.address());
        existingOrganization.setCity(request.city());
        existingOrganization.setPhoneNumber(request.phoneNumber());
        existingOrganization.setEmail(request.email());
        existingOrganization.setWebsite(request.website());
        organizationRepository.save(existingOrganization);

        return JResponse.success(new SaveResponse(existingOrganization.getId()));
    }

    @Transactional
    @Override
    public JResponse deleteOrganization(User currentUser, Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new NotfoundException("Organization not found with id " + id));

        if (!organization.getOwner().equals(currentUser))
            throw new NotfoundException("Organization not found with id " + id);

        organization.setActive(false);
        organizationRepository.save(organization);
        return JResponse.success("Organization deleted");
    }

    @Override
    public Organization getOrganisationByOwner(User currentUser) {
        Organization organization = organizationRepository.findByOwner(currentUser);

        if (organization == null)
            throw new BadRequestException("Your organization not found, please check you your information.");

        return organization;
    }
}
