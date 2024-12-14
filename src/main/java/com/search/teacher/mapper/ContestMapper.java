package com.search.teacher.mapper;

import com.search.teacher.dto.response.ContestResponse;
import com.search.teacher.model.entities.Contest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContestMapper {
    ContestMapper INSTANCE = Mappers.getMapper(ContestMapper.class);

    ContestResponse contestToContestResponse(Contest contest);

    List<ContestResponse> contestsToContestResponses(List<Contest> contests);
}
