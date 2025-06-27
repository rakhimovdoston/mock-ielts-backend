package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "scores")
@Getter
@Setter
public class Score extends BaseEntity {
    private String band;
    private String listening;
    private String reading;
}
