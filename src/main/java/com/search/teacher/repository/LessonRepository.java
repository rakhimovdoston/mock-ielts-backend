package com.search.teacher.repository;

import com.search.teacher.model.entities.Course;
import com.search.teacher.model.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Lesson findByCourseAndId(Course course, Long lessonId);
}
