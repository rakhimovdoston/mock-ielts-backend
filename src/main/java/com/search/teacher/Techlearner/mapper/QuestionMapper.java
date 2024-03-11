package com.search.teacher.Techlearner.mapper;

import com.search.teacher.Techlearner.dto.question.QuestionDto;
import com.search.teacher.Techlearner.model.entities.Question;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuestionDto toDto(Question question);

    Question toEntity(QuestionDto questionDto);

    List<QuestionDto> toListDto(List<Question> questions);
}
