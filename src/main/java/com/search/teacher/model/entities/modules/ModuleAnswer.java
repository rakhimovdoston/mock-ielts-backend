package com.search.teacher.model.entities.modules;

import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.model.entities.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 15:21
 **/
@Entity
@Table(name = "module_answers")
@Getter
@Setter
public class ModuleAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "question_id")
    private Integer questionId;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "reading_id", referencedColumnName = "id")
    private ReadingPassage reading;

//    @ManyToOne
//    @JoinColumn(name = "listening_id", referencedColumnName = "id")
//    private Listening listening;
}
