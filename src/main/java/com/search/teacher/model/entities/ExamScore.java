package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exam_scores")
@Getter
@Setter
public class ExamScore extends BaseEntity {
    private String reading;
    private String listening;
    private String writing;
    private String speaking;
    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private int listeningCount;
    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private int readingCount;

    @OneToOne
    @JoinColumn(name = "mock_test_exam_id", referencedColumnName = "id")
    private MockTestExam mockTestExam;

    private Long assessmentByUserId;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String status;
    private String smsStatus;

    private String pdfId;
}
