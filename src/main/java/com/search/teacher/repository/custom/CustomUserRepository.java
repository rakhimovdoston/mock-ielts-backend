package com.search.teacher.repository.custom;

import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.model.entities.User;

import java.util.List;

public interface CustomUserRepository {
    int countUsers(List<Long> userIds, UserFilter filter);

    List<User> findAllUsersByModuleFilter(List<Long> userIds, UserFilter filter);
}
