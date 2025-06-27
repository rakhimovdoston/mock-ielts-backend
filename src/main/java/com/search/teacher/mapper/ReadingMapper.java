package com.search.teacher.mapper;

import com.search.teacher.dto.response.module.ReadingPassageResponse;
import com.search.teacher.model.entities.Reading;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReadingMapper {

    ReadingMapper INSTANCE = Mappers.getMapper(ReadingMapper.class);

    ReadingPassageResponse toResponse(Reading reading);

    @Mapping(target = "content", ignore = true)
    List<ReadingPassageResponse> toList(List<Reading> readings);

}
