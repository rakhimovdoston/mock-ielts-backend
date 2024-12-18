package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.modules.listening.ListeningModule;
import com.search.teacher.model.entities.modules.listening.ListeningQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Package com.search.teacher.repository.modules
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 15:00
 **/
@Repository
public interface ListeningQuestionRepository extends JpaRepository<ListeningQuestion, Long> {

    ListeningQuestion findByIdAndListening(Long id, ListeningModule listening);
}
