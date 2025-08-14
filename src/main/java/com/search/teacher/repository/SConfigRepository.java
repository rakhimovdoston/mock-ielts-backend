package com.search.teacher.repository;

import com.search.teacher.model.entities.SConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SConfigRepository extends JpaRepository<SConfig, Long> {

    SConfig findByConfigKey(String configKey);
}
