package com.search.teacher.mapper;

import com.search.teacher.dto.response.DescribeDto;
import com.search.teacher.model.entities.Topics;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    DescribeDto toDto(Topics topics);
    List<DescribeDto> toListDto(List<Topics> topics);
}
