package com.search.teacher.repository;

import com.search.teacher.model.entities.MockPackages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockPackagesRepository extends JpaRepository<MockPackages, Long> {

    List<MockPackages> findAllByActiveIsTrueOrderByOrdersAsc();
}
