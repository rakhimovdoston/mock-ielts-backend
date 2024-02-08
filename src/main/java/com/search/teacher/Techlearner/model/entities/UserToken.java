package com.search.teacher.Techlearner.model.entities;

import com.search.teacher.Techlearner.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Entity
@Table(name = "user_tokens")
@Getter
@Setter
public class UserToken extends BaseEntity {
    private String accessToken;
    private String refreshToken;
    private Date expireDate;
    private Date refreshExpireDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}