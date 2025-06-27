package com.search.teacher.repository.custom;

import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.model.entities.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public int countUsers(List<Long> userIds, UserFilter filter) {
        String query = "select count(*) from users where active is true";
        if (filter.getSearch() != null) {
            query += " and (username like :search or firstname like :search or lastname like :search)";
        }

        if (filter.getToDate() != null && filter.getFromDate() != null) {
            query += " and date(created_date) between date(:fromDate) and date(:toDate)";
        } else if (filter.getToDate() != null) {
            query += " and date(test_start_date) = date(:toDate)";
        } else if (filter.getFromDate() != null) {
            query += " and date(test_start_date) = date(:fromDate)";
        }

        if (!userIds.isEmpty()) {
            query += " and user_id in (:userIds)";
        }
        var nativeQuery = entityManager.createNativeQuery(query, Integer.class);
        if (filter.getSearch() != null) {
            nativeQuery.setParameter("search", "%" + filter.getSearch() + "%");
        }
        if (filter.getToDate() != null) {
            nativeQuery.setParameter("toDate", filter.getToDate());
        }
        if (filter.getFromDate() != null) {
            nativeQuery.setParameter("fromDate", filter.getFromDate());
        }
        if (!userIds.isEmpty()) {
            nativeQuery.setParameter("userIds", userIds);
        }
        return (int) nativeQuery.getSingleResult();
    }

    @Override
    public List<User> findAllUsersByModuleFilter(List<Long> userIds, UserFilter filter) {
        String query = "select * from users where active is true";

        if (!filter.getSearch().isEmpty()) {
            query += " and (username like :search or firstname like :search or lastname like :search)";
        }

        if (filter.getToDate() != null && filter.getFromDate() != null) {
            query += " and date(created_date) between date(:fromDate) and date(:toDate)";
        } else if (filter.getToDate() != null) {
            query += " and date(test_start_date) = date(:toDate)";
        } else if (filter.getFromDate() != null) {
            query += " and date(test_start_date) = date(:fromDate)";
        }

        if (!userIds.isEmpty()) {
            query += " and user_id in (:userIds)";
        }

        var nativeQuery = entityManager.createNativeQuery(query, User.class);
        if (!filter.getSearch().isEmpty()) {
            nativeQuery.setParameter("search", "%" + filter.getSearch() + "%");
        }
        if (filter.getToDate() != null) {
            nativeQuery.setParameter("toDate", filter.getToDate());
        }
        if (filter.getFromDate() != null) {
            nativeQuery.setParameter("fromDate", filter.getFromDate());
        }
        if (!userIds.isEmpty()) {
            nativeQuery.setParameter("userIds", userIds);
        }

        return nativeQuery.setFirstResult(filter.getPage() * filter.getSize())
                .setMaxResults(filter.getSize())
                .getResultList();
    }
}
