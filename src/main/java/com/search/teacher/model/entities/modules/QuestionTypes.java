package com.search.teacher.model.entities.modules;

import com.search.teacher.model.enums.ModuleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.model.entities.modules
 * Created by doston.rakhimov
 * Date: 22/06/24
 * Time: 18:30
 **/
@Entity
@Table(name = "question_types")
@Getter
@Setter
public class QuestionTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String descriptionRu;
    private String descriptionEn;

    @Enumerated(EnumType.STRING)
    private ModuleType moduleType;
}
