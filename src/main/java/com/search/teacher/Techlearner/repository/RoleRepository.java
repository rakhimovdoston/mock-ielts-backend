package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Role;
import com.search.teacher.Techlearner.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(RoleType roleName);
}
