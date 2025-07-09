package com.search.teacher.repository;

import com.search.teacher.model.entities.Reading;
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
public interface ReadingRepository extends JpaRepository<Reading, Long> {

    Optional<Reading> findByIdAndUser(Long id, User user);

    Page<Reading> findAllByDeletedIsFalseAndUserAndTypeIn(User user, List<String> types, Pageable pageable);

    @Query(value = "select distinct on (type) * from readings where deleted is false and active is true and type in (:types) and id not in (:ids) and user_id in (:userId) ORDER BY type, random() limit 3", nativeQuery = true)
    List<Reading> findAllRandomAndIdNotInAndUserIn(@Param("types") List<String> types, @Param("ids") List<Long> ids, @Param("userId") List<Long> userId);

    @Query(value = "select distinct on (type) * from readings where deleted is false and active is true and type in (:types) ORDER BY type, random() limit 3", nativeQuery = true)
    List<Reading> findAllByTypeIn(List<String> types);

    @Query(value = "select distinct on (type) * from readings where deleted is false and  type in (:types) ORDER BY type, random() limit :limit", nativeQuery = true)
    List<Reading> findAllRandomTypesAndLimit(@Param("types") List<String> types, @Param("limit") int limit);

    int countAllByUser(User currentUser);
}
