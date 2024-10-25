package com.search.teacher.model.entities.modules.reading;

import com.search.teacher.dto.modules.RMultipleChoiceDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Package com.search.teacher.model.entities.modules.reading
 * Created by doston.rakhimov
 * Date: 25/10/24
 * Time: 13:48
 **/
@Entity
@Table(name = "rmultiple_choices")
@Getter
@Setter
public class RMultipleChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(columnDefinition = "integer default 0")
    private int sort = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> choices = new ArrayList<>();

    private String correctAnswer;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private ReadingQuestion question;

    public RMultipleChoiceDto toDto() {
        RMultipleChoiceDto dto = new RMultipleChoiceDto();
        dto.setId(id);
        dto.setName(name);
        dto.setAnswers(choices);
        dto.setOrder(sort);
        return dto;
    }
}
