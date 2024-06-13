package com.search.teacher.repository;

import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.model.entities.User;
import org.springframework.data.domain.Page;


public interface CustomUserRepository {
    Page<User> findAllByFilter(UserFilter filter);
}
