package com.search.teacher.model.entities.modules.reading;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ModuleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Package com.search.teacher.model.entities.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 14:45
 **/
@Entity
@Table(name = "reading_passage")
@Getter
@Setter
public class ReadingPassage extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    private ModuleType type = ModuleType.READING;

    private String title;
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private boolean list = false;

    private int count = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> passages = new ArrayList<>();

    @OneToMany(mappedBy = "passage")
    private List<ReadingQuestion> questions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false, referencedColumnName = "id")
    private Organization organization;
}
