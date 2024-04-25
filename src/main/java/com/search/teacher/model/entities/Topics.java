package com.search.teacher.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "topics")
@Getter
@Setter
//@org.hibernate.annotations.Cache(region = "all_topics", usage = CacheConcurrencyStrategy.READ_ONLY)
public class Topics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
