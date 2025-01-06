package com.search.teacher.model.entities.modules.reading;

import com.search.teacher.dto.modules.ReadingQuestionResponse;
import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ModuleType;
import com.search.teacher.service.JsoupService;
import com.search.teacher.utils.Utils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Package com.search.teacher.model.entities.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 14:45
 **/
@Entity
@Table(name = "reading_passage")
@Getter
@Setter
public class ReadingPassage extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    private ModuleType type = ModuleType.READING;

    private String title;
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private boolean list = false;

    private int count = 0;

    private Date deleteDate;

    @OneToMany(mappedBy = "passage")
    @OrderBy("sort")
    private List<ReadingQuestion> questions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false, referencedColumnName = "id")
    private Organization organization;

    @OneToMany(mappedBy = "passage")
    private List<PassageAnswer> answers = new ArrayList<>();

    public List<ReadingQuestionResponse> toQuestionDto() {
        List<ReadingQuestionResponse> responses = new ArrayList<>();
        for (ReadingQuestion question : getQuestions()) {
            ReadingQuestionResponse response = new ReadingQuestionResponse();
            response.setId(question.getId());
            response.setText(question.getContent());
            response.setTypes(question.getTypes().getDisplayName());
            response.setCount(question.getQuestionCount());
            response.setCondition(JsoupService.replaceInstruction(question.getInstruction(), response.getCount()));
            response.setQuestions(question.getQuestions().stream().peek(form -> form.setAnswer(null)).toList());
            if (question.getTypes().equals(ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS))
                response.setChoices(question.getChoices().stream().map(RMultipleChoice::toDto).toList());

            responses.add(response);
        }
        return responses;
    }
}
