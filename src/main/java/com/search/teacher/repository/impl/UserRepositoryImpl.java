package com.search.teacher.repository.impl;

import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.enums.RoleType;
import com.search.teacher.repository.CustomUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<User> findAllByFilter(UserFilter filter) {

        final boolean sorted = filter.formPageable().getSort().isSorted();
        final boolean hasSearch = StringUtils.isNotBlank(filter.getSearch());

        StringBuilder sql = new StringBuilder("select t from User t where t.active=true ");

        if (hasSearch) {
            sql.append(" and (lower(t.firstName) like :searchKey ")
                    .append(" or lower(t.lastName) like :searchKey) ");

        }


        if (StringUtils.isNotEmpty(filter.getRole())) {
            sql.append(" and t.role.name=:roleType ");
        }

        String countSql = sql.toString().replace("select t", "select count(t)");

        sql.append(" order by ");

        if (sorted) {
            for (Sort.Order order : filter.formPageable().getSort()) {
                sql.append("t.").append(order.getProperty());
                if (order.isDescending()) {
                    sql.append(" desc");
                }
                sql.append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
        }else {
            sql.append(" t.id");
        }

        TypedQuery<User> query = entityManager.createQuery(sql.toString(), User.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql, Long.class);

        if (hasSearch) {
            query.setParameter("searchKey", filter.getSearchForQuery());
            query.setParameter("searchKey", filter.getSearchForQuery());
        }

        if (StringUtils.isNotEmpty(filter.getRole())) {
            query.setParameter("roleType", RoleType.getRoleByName(filter.getRole()));
            countQuery.setParameter("roleType", RoleType.getRoleByName(filter.getRole()));
        }

        return new PageImpl<>(query.getResultList(), filter.formPageable(), countQuery.getSingleResult());
    }
}
