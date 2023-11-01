package com.search.teacher.Techlearner.model.audit;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@EqualsAndHashCode(exclude = {"createdBy", "updatedBy"})
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class UserAudit {

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

}