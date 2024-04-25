package com.search.teacher.repository;

import com.search.teacher.components.Constants;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.Teacher;
import com.search.teacher.model.entities.User;
import com.search.teacher.utils.ResponseMessage;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByIdAndActive(Long id, boolean active);

    @Cacheable(cacheNames = Constants.TEACHER_BY_USER, key = "#user.id")
    Teacher findByUser(User user);


    default Teacher findNotFoundTeacher(Long id, boolean active) {
        Teacher teacher = this.findByIdAndActive(id, active);
        if (teacher == null)
            throw new NotfoundException(ResponseMessage.TEACHER_NOT_FOUND);
        return teacher;
    }

    default Teacher findTeacherByUser(User user) {
        Teacher teacher = this.findByUser(user);
        if (teacher == null)
            throw new NotfoundException(ResponseMessage.TEACHER_NOT_FOUND);
        return teacher;
    }
}
