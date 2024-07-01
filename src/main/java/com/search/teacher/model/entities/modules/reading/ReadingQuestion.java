package com.search.teacher.model.entities.modules.reading;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.entities.modules.QuestionTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    private String title;
    private boolean titleHtml;

    @Column(columnDefinition = "TEXT")
    private String explanation;
    private boolean explanationHtml;

    private String condition;
    private boolean conditionHtml;

    @OneToMany(mappedBy = "question")
    private List<ReadingAnswer> questions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private QuestionTypes type;

    @ManyToOne
    @JoinColumn(name = "passage_id")
    private ReadingPassage passage;
}
