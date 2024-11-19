package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import com.search.teacher.model.entities.modules.reading.ReadingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Package com.search.teacher.repository.modules
 * Created by doston.rakhimov
 * Date: 15/10/24
 * Time: 12:10
 **/
@Repository
public interface ReadingQuestionRepository extends JpaRepository<ReadingQuestion, Long> {
    ReadingQuestion findByIdAndPassage(Long id, ReadingPassage passage);

    List<ReadingQuestion> findAllByIdInAndPassage(List<Long> ids, ReadingPassage passage);
}
