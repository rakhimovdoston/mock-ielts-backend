package com.search.teacher.repository;

import com.search.teacher.model.entities.CheckWriting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckWritingRepository extends JpaRepository<CheckWriting, Long> {
}
