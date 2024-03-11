package com.search.teacher.Techlearner.mapper;

import com.search.teacher.Techlearner.dto.question.AnswerDto;
import com.search.teacher.Techlearner.model.entities.Answer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    AnswerDto toDto(Answer answer);
    Answer toEntity(AnswerDto answerDto);

    List<AnswerDto> toListDto(List<Answer> answers);

    List<Answer> toListEntity(List<AnswerDto> answers);
}
