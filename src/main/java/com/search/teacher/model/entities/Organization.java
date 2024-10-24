package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Organization extends BaseEntity {

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private String phoneNumber;

    private String email;

    private String website;
    private String address;
    private String longitude;
    private String latitude;
    private String city;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization that)) return false;
        return Objects.equals(getPhoneNumber(), that.getPhoneNumber()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getOwner(), that.getOwner()) &&
                Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhoneNumber(), getEmail(), getOwner(), getId());
    }
}
