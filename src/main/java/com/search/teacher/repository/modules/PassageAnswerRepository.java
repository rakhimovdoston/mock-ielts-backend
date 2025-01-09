package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.modules.listening.ListeningModule;
import com.search.teacher.model.entities.modules.reading.PassageAnswer;
import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassageAnswerRepository extends JpaRepository<PassageAnswer, Long> {

    List<PassageAnswer> findAllByListening(ListeningModule listeningModule);

    boolean existsAllByListening(ListeningModule listening);

    List<PassageAnswer> findAllByPassage(ReadingPassage passage);


    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM RMultipleChoice r " +
            "WHERE r.listening.id = :listeningId AND r.question.id = :questionId")
    boolean isMultipleChoice(@Param("listeningId") Long listeningId, @Param("questionId") Long questionId);
}
