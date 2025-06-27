package com.search.teacher.repository;

import com.search.teacher.model.entities.Listening;
import com.search.teacher.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListeningRepository extends JpaRepository<Listening, Long> {

    Optional<Listening> findByIdAndUser(Long id, User user);

    Page<Listening> findAllByDeletedIsFalseAndUserAndTypeIn(User user, List<String> types, Pageable pageable);

    Page<Listening> findAllByUserIdInAndTypeIn(List<Long> userIds, List<String> types, Pageable pageable);

    @Query(value = "select distinct on (type) * from listening where deleted is false and active is true and type in (:types) and id not in (:ids) and user_id in (:userId) ORDER BY type, random() limit 4", nativeQuery = true)
    List<Listening> findAllRandomAndIdNotInAndUserIn(@Param("types") List<String> types, @Param("ids") List<Long> ids, @Param("userId") List<Long> userId);

    @Query(value = "select distinct on (type) * from listening where deleted is false and type in (:types) ORDER BY type, random() limit :limit", nativeQuery = true)
    List<Listening> findAllRandomTypeAndLimit(List<String> types, int limit);

    int countAllByUser(User currentUser);
}
