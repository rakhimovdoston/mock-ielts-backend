package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.model.entities
 * Created by doston.rakhimov
 * Date: 17/10/24
 * Time: 17:49
 **/
@Entity
@Table(name = "images")
@Getter
@Setter
public class Image extends BaseEntity {
    private String filename;
    private String url;
    private int fileSize = 0;
    private String contentType;
    private String type;
}
