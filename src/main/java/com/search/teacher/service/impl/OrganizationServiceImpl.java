package com.search.teacher.service.impl;

import com.search.teacher.dto.request.OrganizationRequest;
import com.search.teacher.dto.response.OrgResponse;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.filter.OrgFilter;
import com.search.teacher.model.entities.Images;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.ImageRepository;
import com.search.teacher.repository.OrganizationRepository;
import com.search.teacher.service.organization.OrganizationService;
import com.search.teacher.utils.SecurityUtils;
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
    public JResponse getAllOrganizations(OrgFilter filter) {
        Page<Organization> orgPaged = organizationRepository.findAllByFilter(filter);

        List<OrgResponse> responseData = orgPaged.getContent().stream()
                .map(organization -> OrgResponse.builder()
                        .name(organization.getName())
                        .description(organization.getDescription())
                        .registrationNumber(organization.getRegistrationNumber())
                        .address(organization.getAddress())
                        .city(organization.getCity())
                        .phoneNumber(organization.getPhoneNumber())
                        .email(organization.getEmail())
                        .website(organization.getWebsite())
                        .imageUrl(organization.getImages() != null ? organization.getImages().getUrl() : null)
                        .contactPerson(organization.getContactPerson())
                        .contactPersonPhone(organization.getContactPersonPhone())
                        .contactPersonEmail(organization.getContactPersonEmail())
                        .establishedDate(organization.getEstablishedDate())
                        .owner(getOwnerName(organization.getOwner()))
                        .build()
                ).toList();


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
                .images(imageRepository.findById(request.logoId()).orElseThrow())
                .contactPerson(request.contactPerson())
                .contactPersonPhone(request.contactPersonPhone())
                .contactPersonEmail(request.contactPersonEmail())
                .establishedDate(request.establishedDate())
                .owner(securityUtils.currentUser())
                .build();
        organizationRepository.save(organization);

        return JResponse.success("Organization created");
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

        if (request.logoId() != null) {
            Images images = imageRepository.findById(request.logoId()).orElseThrow();
            existingOrganization.setImages(images);
        }

        existingOrganization.setContactPerson(request.contactPerson());
        existingOrganization.setContactPersonPhone(request.contactPersonPhone());
        existingOrganization.setContactPersonEmail(request.contactPersonEmail());
        existingOrganization.setEstablishedDate(request.establishedDate());
        return JResponse.success("Organization updated");
    }

    @Transactional
    @Override
    public JResponse deleteOrganization(Long id) {
        organizationRepository.deleteById(id);
        return JResponse.success("Organization deleted");
    }
}
