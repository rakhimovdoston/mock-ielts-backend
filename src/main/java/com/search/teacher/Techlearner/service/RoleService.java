package com.search.teacher.Techlearner.service;

import com.search.teacher.Techlearner.model.entities.Role;
import com.search.teacher.Techlearner.model.enums.RoleType;
import com.search.teacher.Techlearner.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Role getRoleByName(RoleType roleType) {
        logger.info("Get Role");
        return roleRepository.findByName(roleType);
    }
}
