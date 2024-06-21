package com.search.teacher.model.entities.modules;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.Teacher;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ModuleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.model.entities.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 14:45
 **/
@Entity
@Table(name = "listenings")
@Getter
@Setter
public class Listening extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    private ModuleType type = ModuleType.LISTENING;

    @Column(columnDefinition = "TEXT")
    private String text;

    private String audioUrl;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false, referencedColumnName = "id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false, referencedColumnName = "id")
    private Organization organization;
}
