package com.search.teacher.model.entities.modules.reading;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.entities.modules.listening.ListeningModule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "passage_answers")
@Getter
@Setter
public class PassageAnswer extends BaseEntity {
    private String answer;
    private Long questionId;
    private String questionIds;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "passage_id", referencedColumnName = "id")
    private ReadingPassage passage;

    @ManyToOne
    @JoinColumn(name = "listening_id", referencedColumnName = "id")
    private ListeningModule listening;
}
