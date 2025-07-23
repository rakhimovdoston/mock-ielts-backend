package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "branches")
@Getter
@Setter
public class Branch extends BaseEntity {
    private String name;
    private String description;
    private String location;
    private int maxStudents;
    private String speakingFullName;
}
