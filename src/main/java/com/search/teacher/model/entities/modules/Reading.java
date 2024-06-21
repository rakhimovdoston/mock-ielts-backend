package com.search.teacher.model.entities.modules;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.entities.Teacher;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ModuleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Package com.search.teacher.model.entities.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 14:45
 **/
@Entity
@Table(name = "readings")
@Getter
@Setter
public class Reading extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    private ModuleType type = ModuleType.READING;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @OneToMany(mappedBy = "reading")
    private List<ModuleAnswer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false, referencedColumnName = "id")
    private Teacher teacher;
}
