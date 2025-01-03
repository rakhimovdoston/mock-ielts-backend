package com.search.teacher.mapper;

import com.search.teacher.dto.modules.PassageAnswerDto;
import com.search.teacher.model.entities.modules.reading.PassageAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PassageAnswerMapper {

    PassageAnswerMapper INSTANCE = Mappers.getMapper(PassageAnswerMapper.class);

    @Mapping(target = "id", source = "questionId")
    @Mapping(target = "text", source = "answer")
    @Mapping(target = "count", source = "questionIds")
    PassageAnswerDto toDto(PassageAnswer passageAnswer);

    List<PassageAnswerDto> toListDto(List<PassageAnswer> passageAnswers);
}
