package com.search.teacher.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.dto
 * Created by doston.rakhimov
 * Date: 22/10/24
 * Time: 17:11
 **/
@Getter
@Setter
public class ImageDto {
    private String url;
    private Long id;

    public ImageDto(Long id, String url) {
        this.id = id;
        this.url = url;
    }
}
