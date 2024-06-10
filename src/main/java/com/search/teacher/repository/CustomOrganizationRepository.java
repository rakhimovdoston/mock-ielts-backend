package com.search.teacher.repository;

import com.search.teacher.dto.filter.OrganizationFilter;
import com.search.teacher.model.entities.Organization;
import org.springframework.data.domain.Page;

public interface CustomOrganizationRepository {
    Page<Organization> findAllByFilter(OrganizationFilter filter);
}
