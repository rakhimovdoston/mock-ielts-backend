package com.search.teacher.repository.modules;

import com.search.teacher.dto.modules.ReadingPassageDto;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import com.search.teacher.model.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Package com.search.teacher.repository.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 15:06
 **/
public interface ReadingRepository extends JpaRepository<ReadingPassage, Long> {

    ReadingPassage findByIdAndOrganization(Long id, Organization organization);

    @Query(value = "select new com.search.teacher.dto.modules.ReadingPassageDto(id, difficulty, title, description, SUBSTRING(content, 9, 200),list, active) from ReadingPassage where organization=?1")
    Page<ReadingPassageDto> findAllOrganization(Organization organization, Pageable pageable);

    @Query(value = "select new com.search.teacher.dto.modules.ReadingPassageDto(id, difficulty, title, description, SUBSTRING(content, 9, 200),list, active) from ReadingPassage where organization=?1 and difficulty=?2")
    Page<ReadingPassageDto> findAllOrganizationAndDifficulty(Organization organization, Difficulty difficulty, Pageable pageable);

    List<ReadingPassage> findAllOrganizationAndDifficulty(Organization organization, Difficulty difficulty);




    @Query(value = "select distinct difficulty from ReadingPassage where active and id in (?1) and organization=?2")
    List<Difficulty> findDifficultiesByIdInAndOrganization(List<Long> ids, Organization organization);

}
