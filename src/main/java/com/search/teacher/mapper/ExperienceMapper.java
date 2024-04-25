package com.search.teacher.mapper;

import com.search.teacher.dto.request.ExperienceRequest;
import com.search.teacher.model.entities.Experience;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExperienceMapper {
    Experience toEntity(ExperienceRequest request);
}
