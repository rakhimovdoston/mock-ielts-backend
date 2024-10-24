package com.search.teacher.model.entities.modules.writing;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.entities.Organization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Package com.search.teacher.model.entities.modules.writing
 * Created by doston.rakhimov
 * Date: 22/10/24
 * Time: 17:53
 **/
@Entity
@Table(name = "writing_modules")
@Getter
@Setter
public class WritingModule extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String content;

    private String imageUrl;

    private boolean taskOne;

    private Date deleteDate;

    @ManyToOne
    @JoinColumn(
            name = "organization_id",
            referencedColumnName = "id"
    )
    private Organization organization;
}
