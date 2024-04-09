package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.components.Constants;
import com.search.teacher.Techlearner.model.entities.Question;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Cacheable(cacheNames = Constants.QUESTION_CACHE)
    List<Question> findAllByActiveIsTrue();

    @Cacheable(cacheNames = Constants.QUESTION_CACHE, key = "#id")
    Question findByIdAndActiveIsTrue(Long id);

    List<Question> findAllByActiveIsTrueAndIdIn(List<Long> ids);
}
