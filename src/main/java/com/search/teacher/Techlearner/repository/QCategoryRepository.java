package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.QCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QCategoryRepository extends JpaRepository<QCategory, Long> {
    QCategory findByName(String name);
}
