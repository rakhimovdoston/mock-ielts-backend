package com.search.teacher.model.entities.modules;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ModuleType;
import com.search.teacher.model.enums.TaskType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.model.entities.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 14:54
 **/
@Entity
@Table(name = "writings")
@Getter
@Setter
public class Writing extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    private ModuleType type = ModuleType.LISTENING;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;
}
