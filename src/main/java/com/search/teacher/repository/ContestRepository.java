package com.search.teacher.repository;

import com.search.teacher.model.entities.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Package com.search.teacher.repository
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 12:24
 **/
@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
}
