package com.search.teacher.repository;

import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long>, CustomOrganisationsRepository {

    boolean existsByRegistrationNumber(String registrationNumber);

    Organization findByOwner(User owner);
}
