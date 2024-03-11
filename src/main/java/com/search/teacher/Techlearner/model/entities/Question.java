package com.search.teacher.Techlearner.model.entities;

import com.search.teacher.Techlearner.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
public class Question extends BaseEntity {
    private String name;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();
}
