package com.search.teacher.repository;

import com.search.teacher.dto.filter.OrgFilter;
import com.search.teacher.model.entities.Organization;
import org.springframework.data.domain.Page;

public interface CustomOrgRepository {
    Page<Organization> findAllByFilter(OrgFilter filter);
}
