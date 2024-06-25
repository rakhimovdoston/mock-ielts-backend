package com.search.teacher.model.entities.modules.reading;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.model.entities.modules
 * Created by doston.rakhimov
 * Date: 22/06/24
 * Time: 18:32
 **/
@Entity
@Table(name = "reading_answers")
@Getter
@Setter
public class ReadingAnswer extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "passage_id", referencedColumnName = "id")
    private ReadingPassage passage;

    @Column(columnDefinition = "TEXT")
    private String answers;
}
