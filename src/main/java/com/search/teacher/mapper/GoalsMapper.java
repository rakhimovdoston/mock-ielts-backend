package com.search.teacher.mapper;

import com.search.teacher.dto.response.DescribeDto;
import com.search.teacher.model.entities.Goals;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoalsMapper {

    DescribeDto toDto(Goals goals);
    List<DescribeDto> toListDto(List<Goals> goals);
}
