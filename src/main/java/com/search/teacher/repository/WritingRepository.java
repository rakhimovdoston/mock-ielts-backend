package com.search.teacher.repository;

import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.Writing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WritingRepository extends JpaRepository<Writing, Long> {

    Optional<Writing> findByUserAndId(User user, Long id);

    Page<Writing> findAllByUserAndActiveIsTrueAndDeletedIsFalse(User user, Pageable pageable);

    Page<Writing> findAllByUserAndActiveIsTrueAndTaskAndDeletedIsFalse(User user, boolean task, Pageable pageable);

    @Query(value = "select distinct on (task) * from writings where deleted is false and active is true and task in (:tasks) and id not in (:ids) and user_id in (:userId) ORDER BY task, random() limit 2", nativeQuery = true)
    List<Writing> findAllRandomAndIdNotInAndUserIn(@Param("tasks") List<Boolean> tasks, @Param("ids") List<Long> ids, @Param("userId") List<Long> userId);

    @Query(value = "select w from Writing w where w.deleted is false and w.task = :task order by random() limit 1")
    Writing findRandomByType(@Param("task") Boolean task);

    int countAllByUser(User currentUser);
}
