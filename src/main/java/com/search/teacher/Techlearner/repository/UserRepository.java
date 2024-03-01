package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "user_by_email")
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    @Cacheable(key = "#email")
    User findByEmail(String email);
}
