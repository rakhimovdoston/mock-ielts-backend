package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Rating;
import com.search.teacher.Techlearner.model.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByTeacher(Teacher teacher);

//    @Query(value = "select rating from Rating where Teacher.id=:teacherId")
//    List<Double> findAllRating(Long teacherId);
}
