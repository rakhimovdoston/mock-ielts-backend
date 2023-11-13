package com.search.teacher.Techlearner.model.entities;

import com.search.teacher.Techlearner.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "verification_token")
@NoArgsConstructor
public class VerificationToken extends BaseEntity {
    private String token;
    private Date expirationTime;
    private String refreshToken;
    private String refreshExpirationTime;
    @Transient
    private static final int EXPIRATION_TIME = 5;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public VerificationToken(String token) {
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.DAY_OF_WEEK, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
