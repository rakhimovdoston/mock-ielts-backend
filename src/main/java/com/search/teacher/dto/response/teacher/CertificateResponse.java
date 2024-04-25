package com.search.teacher.dto.response.teacher;

import com.search.teacher.model.enums.CertificateType;
import com.search.teacher.model.enums.TestType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CertificateResponse {
    private Long id;
    private String url;
    private Double listening;
    private Double reading;
    private Double writing;
    private Double speaking;
    private Double overall;
    private CertificateType certificateType;
    private TestType testType;
}
