package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "writings")
@Getter
@Setter
public class Writing extends BaseEntity {
    @Column(columnDefinition = "TEXT")
    private String title;
    private boolean task = false;
    private String image;
    @Column(columnDefinition = "BOOLEAN default FALSE")
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String imageDescription;
}
