package com.search.teacher.Techlearner.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "goals")
@Getter
@Setter
//@org.hibernate.annotations.Cache(region = "all_goals", usage = CacheConcurrencyStrategy.READ_ONLY)
public class Goals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
