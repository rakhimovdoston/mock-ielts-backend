package com.search.teacher.model.entities.modules.listening;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.entities.modules.reading.Form;
import com.search.teacher.model.entities.modules.reading.ReadingQuestionTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Package com.search.teacher.model.entities.modules.listening
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 14:52
 **/
@Entity
@Table(name = "listening_questions")
@Getter
@Setter
public class ListeningQuestion extends BaseEntity {

    private String instruction;

    private boolean html;
    private String content;

    @Column(name = "image_url", length = 200)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ReadingQuestionTypes types;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Form> qustions = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Form> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "listening_id", referencedColumnName = "id")
    private ListeningModule listening;
}
