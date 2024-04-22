package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Course;
import com.search.teacher.Techlearner.model.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Lesson findByCourseAndId(Course course, Long lessonId);
}
