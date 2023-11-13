package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Describe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescribeRepository extends JpaRepository<Describe, Long> {
}
