package com.search.teacher.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "describes")
@Getter
@Setter
//@org.hibernate.annotations.Cache(region = "all_describe", usage = CacheConcurrencyStrategy.READ_ONLY)
public class Describe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
