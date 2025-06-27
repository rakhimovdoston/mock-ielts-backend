package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "module_answers")
@Getter
@Setter
public class ModuleAnswer extends BaseEntity {

    private Long key;
    private String value;

    private String keys;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> values = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "reading_id", referencedColumnName = "id")
    private Reading reading;

    @ManyToOne
    @JoinColumn(name = "listening_id", referencedColumnName = "id")
    private Listening listening;
}
