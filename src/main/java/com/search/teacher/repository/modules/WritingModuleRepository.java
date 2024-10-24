package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.modules.writing.WritingModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Package com.search.teacher.repository.modules
 * Created by doston.rakhimov
 * Date: 22/10/24
 * Time: 17:58
 **/
@Repository
public interface WritingModuleRepository extends JpaRepository<WritingModule, Long> {

    WritingModule findByIdAndActiveTrueAndOrganization(Long id, Organization organization);

    Page<WritingModule> findAllByActiveTrueAndOrganization(Organization organization, Pageable pageable);

    Page<WritingModule> findAllByActiveTrueAndOrganizationAndTaskOne(Organization organization, boolean taskOne, Pageable pageable);
}
