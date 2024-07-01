package com.search.teacher.service.impl;

import com.search.teacher.dto.UserDto;
import com.search.teacher.dto.filter.OrganizationFilter;
import com.search.teacher.dto.request.OrganizationRequest;
import com.search.teacher.dto.response.OrganizationResponse;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.ImageRepository;
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
    private final SecurityUtils securityUtils;
    private final ImageRepository imageRepository;

    @Override
    public JResponse getAllOrganizations(OrganizationFilter filter) {
        Page<Organization> orgPaged = organizationRepository.findAllByFilter(filter);

        List<OrganizationResponse> responseData = orgPaged.getContent().stream()
                .map(organization -> OrganizationResponse.builder()
                        .name(organization.getName())
                        .description(organization.getDescription())
                        .registrationNumber(organization.getRegistrationNumber())
                        .address(organization.getAddress())
                        .city(organization.getCity())
                        .phoneNumber(organization.getPhoneNumber())
                        .email(organization.getEmail())
                        .website(organization.getWebsite())
                        .owner(getOwnerName(organization.getOwner()))
                        .build()
                ).toList();

        if (Collections.isEmpty(responseData))
            return JResponse.error(404, "Organisations not found");

        return JResponse.success(responseData);
    }

    private String getOwnerName(User owner) {
        return owner.getFirstname() + " " + owner.getLastname();
    }

    @Transactional
    @Override
    public JResponse createOrganization(OrganizationRequest request) {

        Organization organization = Organization.builder()
                .name(request.name())
                .registrationNumber(request.registrationNumber())
                .address(request.address())
                .city(request.city())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .website(request.website())
                .owner(securityUtils.getCurrentUser())
                .build();

        organizationRepository.save(organization);
        return JResponse.success(new SaveResponse(organization.getId()));
    }

    @Transactional
    @Override
    public JResponse update(OrganizationRequest request) {
        Organization existingOrganization = organizationRepository.findById(request.id())
                .orElseThrow(() -> new NotfoundException("Organization not found with id " + request.id()));

        existingOrganization.setName(request.name());
        existingOrganization.setRegistrationNumber(request.registrationNumber());
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
    public JResponse deleteOrganization(Long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow();
        organization.setActive(false);
        organizationRepository.save(organization);
        return JResponse.success("Organization deleted");
    }

    @Override
    public void createOrganisation(UserDto userDto, User user) {
        if (organizationRepository.existsByRegistrationNumber(userDto.registrationNumber()))
            throw new NotfoundException("Registration number already exists");

        Organization organization = new Organization();
        organization.setName(userDto.name());
        organization.setOwner(user);
        organization.setEmail(user.getEmail());
        organization.setDescription(userDto.description());
        organization.setRegistrationNumber(userDto.registrationNumber());
        organizationRepository.save(organization);
    }
}
