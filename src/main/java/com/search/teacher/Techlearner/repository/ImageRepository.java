package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Images;
import com.search.teacher.Techlearner.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Images, Long> {

    List<Images> findAllByIdIn(List<Long> ids);

    List<Images> findByUser(User user);

    Images findByUserAndFilename(User user, String filename);

    Images findByIdAndUser(Long id, User user);
}
