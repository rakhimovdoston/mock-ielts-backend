package com.search.teacher.repository;

import com.search.teacher.components.Constants;
import com.search.teacher.model.entities.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    @Cacheable(cacheNames = Constants.USER_EMAIL, key = "#email")
    User findByEmail(String email);
}
