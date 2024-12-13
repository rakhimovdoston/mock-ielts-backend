package com.search.teacher.model.entities.modules.reading;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "passage_answers")
@Getter
@Setter
public class PassageAnswer extends BaseEntity {
    private String answer;
    private Long id;

    @ManyToOne
    @JoinColumn(name = "passage_id", referencedColumnName = "id")
    private ReadingPassage passage;
}
