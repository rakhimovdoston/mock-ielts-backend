package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "test_sessions")
@Getter
@Setter
public class TestSession extends BaseEntity {
    private Long userId;
    private Long studentId;
    private Date enterTime;
}
