package com.search.teacher.model.entities;

import com.search.teacher.dto.ai.WritingAIFeedback;
import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "check_writings")
@Getter
@Setter
public class CheckWriting extends BaseEntity {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private WritingAIFeedback response;

    private Double score;
    @Column(columnDefinition = "TEXT")
    private String summary;

    @OneToOne
    @JoinColumn(name = "user_writing_answer_id", referencedColumnName = "id")
    private UserWritingAnswer userWritingAnswer;
}
