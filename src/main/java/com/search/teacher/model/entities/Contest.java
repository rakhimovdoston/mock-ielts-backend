package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.model.entities
 * Created by doston.rakhimov
 * Date: 22/10/24
 * Time: 11:56
 **/
@Entity
@Table(name = "contests")
@Getter
@Setter
public class Contest extends BaseEntity {

    private String name;
    private String description;
    private String period;
}
