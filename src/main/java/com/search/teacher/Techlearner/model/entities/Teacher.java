package com.search.teacher.Techlearner.model.entities;

import com.search.teacher.Techlearner.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
public class Teacher extends BaseEntity {

    private String title;
    private String profession;
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String description;
    private Double rating;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Long> topics = new ArrayList<>();

    @OneToMany(mappedBy = "teacher")
    private List<Certificate> certificates;

    @OneToMany
    private List<Experience> experiences = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany
    private List<Education> educations = new ArrayList<>();
}
