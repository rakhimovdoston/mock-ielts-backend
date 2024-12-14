package com.search.teacher.repository;

import com.search.teacher.model.entities.Contest;
import com.search.teacher.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Package com.search.teacher.repository
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 12:24
 **/
@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

    Page<Contest> findAllByUser(User user, Pageable pageable);

    Optional<Contest> findByIdAndUser(Long id, User user);
}
