package com.search.teacher.mapper;

import com.search.teacher.dto.modules.QuestionTypeDto;
import com.search.teacher.model.entities.modules.reading.QuestionTypes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Package com.search.teacher.mapper
 * Created by doston.rakhimov
 * Date: 24/10/24
 * Time: 11:01
 **/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionTypesMapper {

    QuestionTypeDto questionTypesToQuestionTypeDto(QuestionTypes questionTypes);

    List<QuestionTypeDto> questionTypesToQuestionTypeDtoList(List<QuestionTypes> questionTypes);
}
