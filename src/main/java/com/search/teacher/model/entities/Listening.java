package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "listening")
@Getter
@Setter
public class Listening extends BaseEntity {
    private String title;
    private String audio;
    private String type;

    @Column(columnDefinition = "BOOLEAN default FALSE")
    private boolean deleted;

    @OneToMany(mappedBy = "listening")
    @OrderBy("orders ASC")
    private List<ModuleQuestions> questions = new ArrayList<>();

    @OneToMany(mappedBy = "listening")
    private List<ModuleAnswer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
