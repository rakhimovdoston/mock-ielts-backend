package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "mock_test_exam")
@Getter
@Setter
public class MockTestExam extends BaseEntity {
    private String name;
    private Date testDate;
    private Date submittedDate;
    private Date startDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Long> readings = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Long> listening = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Long> writings = new ArrayList<>();

    @OneToOne(mappedBy = "mockTestExam")
    private ExamScore score;

    @OneToMany(mappedBy = "mockTestExam")
    private List<UserExamAnswers> userExamAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "mockTestExam")
    private List<UserWritingAnswer> writingAnswers = new ArrayList<>();

}
