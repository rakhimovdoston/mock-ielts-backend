package com.search.teacher.Techlearner.service;

import com.search.teacher.Techlearner.components.Constants;
import com.search.teacher.Techlearner.model.entities.Role;
import com.search.teacher.Techlearner.model.enums.RoleType;
import com.search.teacher.Techlearner.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Cacheable(cacheNames = Constants.ROLE_NAME, key = "#roleType.name()")
    public Role getRoleByName(RoleType roleType) {
        logger.info("Get Role: {}", roleType.name());
        return roleRepository.findByName(roleType);
    }

    @Cacheable(cacheNames = Constants.ROLE_NAME)
    public List<Role> getAllRoles() {
        logger.info("Get All Roles");
        return roleRepository.findAll();
    }
}
