package com.search.teacher.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "user_answers")
@Getter
@Setter
public class UserAnswers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long key;
    private String value;
    private String keys;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> values;

    @ManyToOne
    @JoinColumn(name = "user_exam_answers_id", referencedColumnName = "id")
    private UserExamAnswers userExamAnswers;
}
