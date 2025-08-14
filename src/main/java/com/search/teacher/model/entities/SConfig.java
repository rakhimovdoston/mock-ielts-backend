package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "s_config")
@Getter
@Setter
public class SConfig extends BaseEntity {

    private String configKey;
    private String value;
    private String description;
}
