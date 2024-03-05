package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.components.Constants;
import com.search.teacher.Techlearner.model.entities.Teacher;
import com.search.teacher.Techlearner.model.entities.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByIdAndActive(Long id, boolean active);

    @Cacheable(cacheNames = Constants.TEACHER_BY_USER, key = "#user.id")
    Teacher findByUser(User user);
}
