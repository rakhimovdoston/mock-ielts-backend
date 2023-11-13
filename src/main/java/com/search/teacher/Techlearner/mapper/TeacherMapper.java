package com.search.teacher.Techlearner.mapper;

import com.search.teacher.Techlearner.dto.response.TeacherResponse;
import com.search.teacher.Techlearner.model.entities.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherMapper {
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "certificates", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "educations", ignore = true)
    TeacherResponse toResponse(Teacher teacher);
}
