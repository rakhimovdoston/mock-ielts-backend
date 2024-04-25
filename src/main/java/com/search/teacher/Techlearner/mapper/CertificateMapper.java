package com.search.teacher.Techlearner.mapper;

import com.search.teacher.Techlearner.dto.response.teacher.CertificateResponse;
import com.search.teacher.Techlearner.model.entities.Certificate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    CertificateResponse toResponse(Certificate certificate);

    List<CertificateResponse> toResponseList(List<Certificate> certificates);

}
