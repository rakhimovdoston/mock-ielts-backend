package com.search.teacher.repository;

import com.search.teacher.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
