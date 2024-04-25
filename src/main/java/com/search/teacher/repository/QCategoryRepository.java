package com.search.teacher.repository;

import com.search.teacher.model.entities.QCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QCategoryRepository extends JpaRepository<QCategory, Long> {
    QCategory findByName(String name);
}
