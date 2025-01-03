package com.search.teacher.dto.modules;

import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Package com.search.teacher.dto.modules
 * Created by doston.rakhimov
 * Date: 14/10/24
 * Time: 17:37
 **/
@Getter
@Setter
public class ReadingPassageDto {
    private Long id;
    private Difficulty difficulty;
    private String title;
    private String description;
    private String content;
    private List<String> passages = new ArrayList<>();
    private boolean withList = false;
    private boolean active;
    private List<String> types = new ArrayList<>();

    public ReadingPassageDto() {
    }

    public ReadingPassageDto(Long id,
                             Difficulty difficulty,
                             String title,
                             String description,
                             String content, boolean withList, boolean active) {
        this.id = id;
        this.difficulty = difficulty;
        this.title = title;
        this.description = description;
        this.content = content;
        this.withList = withList;
        this.active = active;
    }
}
