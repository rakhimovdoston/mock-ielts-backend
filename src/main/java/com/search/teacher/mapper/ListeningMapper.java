package com.search.teacher.mapper;

import com.search.teacher.dto.response.module.ListeningResponse;
import com.search.teacher.model.entities.Listening;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListeningMapper {

    ListeningMapper INSTANCE = Mappers.getMapper(ListeningMapper.class);

    ListeningResponse toResponse(Listening listening);

    List<ListeningResponse> toList(List<Listening> listings);
}
