package com.search.teacher.Techlearner.mapper;

import com.search.teacher.Techlearner.dto.response.DescribeDto;
import com.search.teacher.Techlearner.model.entities.Describe;
import com.search.teacher.Techlearner.model.entities.Goals;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DescribeMapper {

    DescribeDto toDto(Describe describe);
    List<DescribeDto> toListDto(List<Describe> describes);
}
