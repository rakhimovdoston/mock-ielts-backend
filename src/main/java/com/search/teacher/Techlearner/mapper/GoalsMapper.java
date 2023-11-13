package com.search.teacher.Techlearner.mapper;

import com.search.teacher.Techlearner.dto.response.DescribeDto;
import com.search.teacher.Techlearner.model.entities.Goals;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoalsMapper {

    DescribeDto toDto(Goals goals);
    List<DescribeDto> toListDto(List<Goals> goals);
}
