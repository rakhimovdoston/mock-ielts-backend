package com.search.teacher.mapper;

import com.search.teacher.dto.response.module.WritingResponse;
import com.search.teacher.model.entities.Writing;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WritingMapper {

    WritingMapper INSTANCE = Mappers.getMapper(WritingMapper.class);

    WritingResponse toResponse(Writing writing);

    List<WritingResponse> toList(List<Writing> writings);
}
