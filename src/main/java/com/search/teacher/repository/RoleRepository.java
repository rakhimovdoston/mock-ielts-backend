package com.search.teacher.repository;

import com.search.teacher.model.entities.Role;
import com.search.teacher.model.enums.RoleType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String roleName);

}
