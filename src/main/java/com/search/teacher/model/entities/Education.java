package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.enums.Degree;
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
    private String entry; //    date(2018-09-01)
    private String end; // date(2022-07-01)
    private String description;
    @Enumerated(EnumType.STRING)
    private Degree degree;

//    @ManyToOne
//    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
//    private Teacher teacher;
}
