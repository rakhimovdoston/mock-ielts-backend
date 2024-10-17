package com.search.teacher.repository;

import com.search.teacher.model.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Package com.search.teacher.repository
 * Created by doston.rakhimov
 * Date: 17/10/24
 * Time: 17:51
 **/
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
