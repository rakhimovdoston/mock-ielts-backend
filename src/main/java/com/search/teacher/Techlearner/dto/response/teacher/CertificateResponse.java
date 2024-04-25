package com.search.teacher.Techlearner.dto.response.teacher;

import com.search.teacher.Techlearner.model.enums.CertificateType;
import com.search.teacher.Techlearner.model.enums.TestType;
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
