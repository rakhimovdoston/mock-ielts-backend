package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Comment;
import com.search.teacher.Techlearner.model.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTeacher(Teacher teacher);
}
