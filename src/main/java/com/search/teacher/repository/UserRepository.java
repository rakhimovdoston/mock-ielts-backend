package com.search.teacher.repository;

import com.search.teacher.model.entities.User;
import com.search.teacher.repository.custom.CustomUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    User findByEmail(String email);

    User findByIdAndActiveIsTrue(Long id);

    User findByIdAndUserId(Long id, Long userId);

    User findByUsername(String username);

    Page<User> findAllByActiveIsTrueAndUserIdIn(List<Long> userIds, Pageable pageable);

    int countAllByUserIdIn(List<Long> userIds);
}
