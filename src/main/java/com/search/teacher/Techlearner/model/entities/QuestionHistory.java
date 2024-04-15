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
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "question_history")
public class QuestionHistory extends BaseEntity {

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ClientAnswer> request;

    @Column(name = "request_id")
    private String requestId = UUID.randomUUID().toString();

    @Column(name = "question_ids", columnDefinition = "jsonb")
    @JdbcTypeCode((SqlTypes.JSON))
    private List<Long> questionIds;

    private Date date;

    private int correctCount;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
