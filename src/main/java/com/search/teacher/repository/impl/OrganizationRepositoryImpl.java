package com.search.teacher.repository.impl;

import com.search.teacher.dto.filter.OrganizationFilter;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.repository.CustomOrganisationsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class OrganizationRepositoryImpl implements CustomOrganisationsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Organization> findAllByFilter(OrganizationFilter filter) {
        final boolean hasSearch = StringUtils.isNotBlank(filter.getSearch());
        final boolean sorted = filter.formPageable().getSort().isSorted();


        StringBuilder sql = new StringBuilder("select t from Organization t where t.active=true ");

        if (hasSearch) {
            sql.append(" and (lower(t.name) like :searchKey ");
            sql.append(" or lower(t.description) like :searchKey)");
        }
        String countSql = sql.toString().replaceFirst("select t", "select count(t)");
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
        } else {
            sql.append(" t.id");
        }

        TypedQuery<Organization> query = entityManager.createQuery(sql.toString(), Organization.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql, Long.class);
        if (hasSearch) {
            query.setParameter("searchKey", filter.getSearchForQuery());
            countQuery.setParameter("searchKey", filter.getSearchForQuery());
        }


        return new PageImpl<>(query.getResultList(), filter.getPageable(), countQuery.getSingleResult());
    }
}
