package com.search.teacher.model.entities.modules.listening;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.enums.Difficulty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Package com.search.teacher.model.entities.modules.listening
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 14:49
 **/
@Entity
@Table(name = "listening_modules")
@Getter
@Setter
public class ListeningModule extends BaseEntity {
    private String title;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private String audio;


    @OneToMany(mappedBy = "listening")
    private List<ListeningQuestion> questions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    private Organization organization;
}
