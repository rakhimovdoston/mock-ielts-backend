package com.search.teacher.repository;

import com.search.teacher.model.entities.BookingGroup;
import com.search.teacher.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingGroupRepository extends JpaRepository<BookingGroup, Long> {
    List<BookingGroup> findAllByUser_Id(Long userId);
}
