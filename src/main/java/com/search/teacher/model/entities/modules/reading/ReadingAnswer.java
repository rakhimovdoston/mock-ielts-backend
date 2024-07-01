package com.search.teacher.model.entities.modules.reading;

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
public class ReadingAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(columnDefinition = "TEXT")
    private String text;
    private boolean html;
    private int order;
    private String answers;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private ReadingQuestion question;
}
