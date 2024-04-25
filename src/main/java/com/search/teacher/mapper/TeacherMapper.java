package com.search.teacher.mapper;

import com.search.teacher.dto.response.TeacherResponse;
import com.search.teacher.model.entities.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherMapper {
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "educations", ignore = true)
    @Mapping(target = "certificates", ignore = true)
    TeacherResponse toResponse(Teacher teacher);
}
