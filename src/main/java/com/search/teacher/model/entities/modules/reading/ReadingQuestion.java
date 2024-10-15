package com.search.teacher.model.entities.modules.reading;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
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

    private String instruction;

    private String content;
    private boolean html = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Form> questions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ReadingQuestionTypes types;

    @ManyToOne
    @JoinColumn(name = "passage_id", referencedColumnName = "id")
    private ReadingPassage passage;
}
