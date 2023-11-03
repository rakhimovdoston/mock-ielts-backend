package com.search.teacher.Techlearner.model.entities;

import com.search.teacher.Techlearner.model.base.BaseEntity;
import com.search.teacher.Techlearner.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String code;
    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean isForgotPassword = false;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;


}
