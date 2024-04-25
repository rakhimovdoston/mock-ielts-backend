package com.search.teacher.mapper;

import com.search.teacher.dto.response.teacher.CertificateResponse;
import com.search.teacher.model.entities.Certificate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    CertificateResponse toResponse(Certificate certificate);

    List<CertificateResponse> toResponseList(List<Certificate> certificates);

}
