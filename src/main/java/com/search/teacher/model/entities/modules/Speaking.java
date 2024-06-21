package com.search.teacher.model.entities.modules;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.enums.TaskType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.model.entities.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 14:58
 **/
@Entity
@Table(name = "speakings")
@Getter
@Setter
public class Speaking extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    private String question;

    @Column(columnDefinition = "TEXT")
    private String topic;
}
