package com.search.teacher.model.entities.modules.reading;

import com.search.teacher.model.enums.ModuleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.model.entities.modules.reading
 * Created by doston.rakhimov
 * Date: 14/10/24
 * Time: 18:12
 **/
@Entity
@Table(name = "question_types")
@Getter
@Setter
public class QuestionTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "boolean default 'false'")
    private boolean active = false;

    private String name;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String example;

    @Enumerated(EnumType.STRING)
    private ReadingQuestionTypes type;

    @Enumerated(EnumType.STRING)
    private ModuleType moduleType;
}
