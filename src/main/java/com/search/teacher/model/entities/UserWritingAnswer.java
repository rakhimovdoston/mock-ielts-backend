package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_writing_answer")
@Getter
@Setter
public class UserWritingAnswer extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String answer;
    private Long userId;
    private Long writingId;

    @ManyToOne
    @JoinColumn(name = "mock_test_exam_id", referencedColumnName = "id")
    private MockTestExam mockTestExam;

    @OneToOne(mappedBy = "userWritingAnswer")
    private CheckWriting checkWriting;
}
