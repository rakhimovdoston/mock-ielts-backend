package com.search.teacher.repository;

import com.search.teacher.model.entities.Describe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescribeRepository extends JpaRepository<Describe, Long> {
}
