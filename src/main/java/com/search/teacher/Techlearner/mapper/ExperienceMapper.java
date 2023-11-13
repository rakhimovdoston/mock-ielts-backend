package com.search.teacher.Techlearner.mapper;

import com.search.teacher.Techlearner.dto.request.ExperienceRequest;
import com.search.teacher.Techlearner.model.entities.Experience;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExperienceMapper {
    Experience toEntity(ExperienceRequest request);
}
