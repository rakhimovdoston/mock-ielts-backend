package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.modules.listening.ListeningModule;
import com.search.teacher.model.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Package com.search.teacher.repository.modules
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 14:50
 **/
@Repository
public interface ListeningModuleRepository extends JpaRepository<ListeningModule, Long> {

    ListeningModule findByIdAndOrganization(Long id, Organization organization);

    @Query(value = "select distinct difficulty from ListeningModule where active and id in (?1) and organization=?2")
    List<Difficulty> findDifficultyByIdAndOrganization(List<Long> ids, Organization organization);

    Page<ListeningModule> findAllByOrganizationAndDifficulty(Organization organization, Difficulty difficulty, Pageable pageable);

    Page<ListeningModule> findAllByOrganization(Organization organization, Pageable pageable);
}
