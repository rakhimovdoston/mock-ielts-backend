package com.search.teacher.Techlearner.model.entities;

import com.search.teacher.Techlearner.model.base.BaseEntity;
import com.search.teacher.Techlearner.model.enums.Degree;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "educations")
@Getter
@Setter
public class Education extends BaseEntity {
    private String name;
    private String url;
    private String faculty;
    @Enumerated(EnumType.STRING)
    private Degree degree;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;
}
