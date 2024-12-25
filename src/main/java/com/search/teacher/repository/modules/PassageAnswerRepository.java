package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.modules.listening.ListeningModule;
import com.search.teacher.model.entities.modules.reading.PassageAnswer;
import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassageAnswerRepository extends JpaRepository<PassageAnswer, Long> {

    List<PassageAnswer> findAllByListening(ListeningModule listeningModule);

    List<PassageAnswer> findAllByPassage(ReadingPassage passage);
}
