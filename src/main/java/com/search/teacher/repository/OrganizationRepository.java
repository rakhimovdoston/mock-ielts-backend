package com.search.teacher.repository;

import com.search.teacher.model.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long>, CustomOrgRepository {
}
