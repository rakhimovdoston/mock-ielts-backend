package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Course;
import com.search.teacher.Techlearner.model.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByTeacherAndId(Teacher teacher, Long id);

    List<Course> findByTeacher(Teacher teacher);
}
