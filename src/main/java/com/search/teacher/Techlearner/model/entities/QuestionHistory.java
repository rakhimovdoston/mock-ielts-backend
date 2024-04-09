package com.search.teacher.Techlearner.model.entities;

import com.search.teacher.Techlearner.dto.question.ClientAnswer;
import com.search.teacher.Techlearner.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "questions_history")
public class QuestionHistory extends BaseEntity {

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ClientAnswer> request;

    private Date date;

    private int correctCount;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
