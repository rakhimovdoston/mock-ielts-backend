package com.search.teacher.Techlearner.model.entities;

import com.search.teacher.Techlearner.model.base.BaseEntity;
import com.search.teacher.Techlearner.model.enums.CertificateType;
import com.search.teacher.Techlearner.model.enums.TestType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "certificates")
@Getter
@Setter
public class Certificate extends BaseEntity {
    private Double reading;
    private Double listening;
    private Double speaking;
    private Double writing;
    private Double overall;
    private String url;
    private CertificateType certificateType;
    private TestType testType;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id", nullable = false)
    private Teacher teacher;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Images image;
}
