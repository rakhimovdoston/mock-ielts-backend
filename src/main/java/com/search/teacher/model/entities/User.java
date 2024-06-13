package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.enums.IStatus;
import com.search.teacher.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "describe_id", referencedColumnName = "id")
    private Describe describe;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Long> topics = new ArrayList<>();

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Long> goals = new ArrayList<>();
}
