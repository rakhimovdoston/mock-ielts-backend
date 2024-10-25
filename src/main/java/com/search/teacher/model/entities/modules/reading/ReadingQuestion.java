package com.search.teacher.model.entities.modules.reading;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Package com.search.teacher.model.entities.modules
 * Created by doston.rakhimov
 * Date: 22/06/24
 * Time: 18:27
 **/

@Entity
@Table(name = "reading_questions")
@Getter
@Setter
public class ReadingQuestion extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String instruction;

    @Column(columnDefinition = "TEXT")
    private String content;
    private boolean html = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Form> questions = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<RMultipleChoice> choices = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ReadingQuestionTypes types;

    private Date deleteDate;

    @ManyToOne
    @JoinColumn(name = "passage_id", referencedColumnName = "id")
    private ReadingPassage passage;

    @Column(columnDefinition = "integer default 0")
    private int sort = 0;
}
