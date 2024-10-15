package com.search.teacher.repository;

import com.search.teacher.model.entities.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    UserToken findByAccessToken(String accessToken);
    UserToken findByRefreshToken(String refreshToken);
}
