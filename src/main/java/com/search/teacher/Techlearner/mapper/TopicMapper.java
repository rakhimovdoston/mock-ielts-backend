package com.search.teacher.Techlearner.mapper;

import com.search.teacher.Techlearner.dto.response.DescribeDto;
import com.search.teacher.Techlearner.model.entities.Topics;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    DescribeDto toDto(Topics topics);
    List<DescribeDto> toListDto(List<Topics> topics);
}
