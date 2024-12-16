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
    private String name;

    public ImageDto(Long id, String url, String name) {
        this.id = id;
        this.url = url;
        this.name = name;
    }
}
