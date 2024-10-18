package com.search.teacher.model.entities;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.enums.ImageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

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
    @Column(name = "object_name")
    private String objectName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "size")
    private Long size;

    @Column(name = "url", columnDefinition = "varchar")
    private String url;

    @Column(name = "image_type")
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    private Long userId;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
