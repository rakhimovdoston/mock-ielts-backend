package com.search.teacher.Techlearner.mapper;

import com.search.teacher.Techlearner.dto.response.StudentResponse;
import com.search.teacher.Techlearner.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "describes", ignore = true)
    @Mapping(target = "goals", ignore = true)
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "role", ignore = true)
    StudentResponse toResponse(User user);
}
