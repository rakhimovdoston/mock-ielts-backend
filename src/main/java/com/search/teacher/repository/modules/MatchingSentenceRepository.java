package com.search.teacher.repository.modules;

import com.search.teacher.model.entities.modules.reading.MatchingSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingSentenceRepository extends JpaRepository<MatchingSentence, Long> {
}
