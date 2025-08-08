package com.search.teacher.repository;

import com.search.teacher.model.entities.ExamScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamScoreRepository extends JpaRepository<ExamScore, Long> {

    @Query("select e from ExamScore e where (e.listeningCount =0 and e.readingCount = 0 and e.writing is not null)")
    Page<ExamScore> findAllByListeningAndReading(Pageable pageable);
}
