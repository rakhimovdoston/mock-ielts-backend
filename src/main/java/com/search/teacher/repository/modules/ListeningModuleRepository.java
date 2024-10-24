package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.modules.listening.ListeningModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Package com.search.teacher.repository.modules
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 14:50
 **/
@Repository
public interface ListeningModuleRepository extends JpaRepository<ListeningModule, Long> {

    ListeningModule findByIdAndOrganization(Long id, Organization organization);
}
