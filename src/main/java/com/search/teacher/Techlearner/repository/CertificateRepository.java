package com.search.teacher.Techlearner.repository;

import com.search.teacher.Techlearner.model.entities.Certificate;
import com.search.teacher.Techlearner.model.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Certificate findByActiveIsTrueAndIdAndTeacher(Long id, Teacher teacher);

    List<Certificate> findAllByActiveIsTrueAndTeacher(Teacher teacher);
}
