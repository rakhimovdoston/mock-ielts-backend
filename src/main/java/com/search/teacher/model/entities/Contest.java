package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Package com.search.teacher.model.entities
 * Created by doston.rakhimov
 * Date: 22/10/24
 * Time: 11:56
 **/
@Entity
@Table(name = "contests")
@Getter
@Setter
public class Contest extends BaseEntity {

    private String name;
    private String description;
    private Date startDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "reading_passage_ids", columnDefinition = "jsonb")
    private List<Long> readingPassageIds = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "listenig_passage_ids", columnDefinition = "jsonb")
    private List<Long> listeningPassageIds = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "writing_passage_ids", columnDefinition = "jsonb")
    private List<Long> writingPassageIds = new ArrayList<>();
}
