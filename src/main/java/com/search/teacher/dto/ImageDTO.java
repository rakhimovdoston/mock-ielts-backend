package com.search.teacher.dto;

import com.search.teacher.model.entities.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ImageDTO {
    UUID id;
    String url;
    String objectName;

    public ImageDTO(UUID id) {
        this.id = id;
    }

    public ImageDTO(Image image) {
        this.url = image.getUrl();
        this.objectName = image.getObjectName();
    }
}