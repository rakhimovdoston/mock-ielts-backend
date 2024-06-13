package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Organization extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private String registrationNumber;

    private String address;

    private String city;

    private String phoneNumber;

    private String email;

    private String website;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    private List<Images> images;

    private String contactPerson;

    private String contactPersonPhone;

    private String contactPersonEmail;

    private LocalDate establishedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    public void addImage(Images image) {
        this.images.add(image);
    }
}
