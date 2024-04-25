package com.search.teacher.Techlearner.dto.request.teacher;

import com.search.teacher.Techlearner.model.enums.CertificateType;
import com.search.teacher.Techlearner.model.enums.TestType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCertificate {
    @NotNull
    private Long imageId;
    private CertificateType certificateType;
    private Double reading;
    private Double listening;
    private Double writing;
    private Double speaking;
    private Double overall;
    private TestType testType;
}
