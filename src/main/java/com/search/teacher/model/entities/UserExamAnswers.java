package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_exam_answers")
@Getter
@Setter
public class UserExamAnswers extends BaseEntity {

    private Long readingId;

    private Long listeningId;
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "mock_test_exam_id", referencedColumnName = "id")
    private MockTestExam mockTestExam;

    @OneToMany(mappedBy = "userExamAnswers")
    private List<UserAnswers> answers = new ArrayList<>();
}
