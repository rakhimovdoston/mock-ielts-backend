package com.search.teacher.mapper;

import com.search.teacher.dto.response.module.ModuleAnswerResponse;
import com.search.teacher.model.entities.ModuleAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModuleAnswerMapper {

    ModuleAnswerMapper INSTANCE = Mappers.getMapper(ModuleAnswerMapper.class);

    ModuleAnswerResponse toResponse(ModuleAnswer moduleAnswer);

    List<ModuleAnswerResponse> toList(List<ModuleAnswer> moduleAnswers);
}
