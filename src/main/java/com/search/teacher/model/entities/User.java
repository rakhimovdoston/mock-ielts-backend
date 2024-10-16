package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.enums.IStatus;
import com.search.teacher.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "users")

public class User extends BaseEntity implements Serializable {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String code;
    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean isForgotPassword = false;
    @Column(name = "internet_status")
    private IStatus internetStatus;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEmail());
    }
}
