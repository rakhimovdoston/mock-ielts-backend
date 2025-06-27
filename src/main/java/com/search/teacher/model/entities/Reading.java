package com.search.teacher.model.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "readings")
@Getter
@Setter
public class Reading extends BaseEntity {
    private String title;
    private String type;
    @Column(columnDefinition = "BOOLEAN default FALSE")
    private boolean deleted;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode content;

    @OneToMany(mappedBy = "reading")
    @OrderBy("orders ASC")
    private List<ModuleQuestions> questions = new ArrayList<>();

    @OneToMany(mappedBy = "reading")
    private List<ModuleAnswer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
