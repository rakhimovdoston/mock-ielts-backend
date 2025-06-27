package com.search.teacher.model.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "module_questions")
@Getter
@Setter
public class ModuleQuestions extends BaseEntity {

    private String categoryType;
    private String categoryName;

    @Column(columnDefinition = "INTEGER default 0")
    private int orders;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode question;

    @ManyToOne
    @JoinColumn(name = "reading_id", referencedColumnName = "id")
    private Reading reading;

    @ManyToOne
    @JoinColumn(name = "listening_id", referencedColumnName = "id")
    private Listening listening;
}
