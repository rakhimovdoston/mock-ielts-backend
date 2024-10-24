package com.search.teacher.dto.modules.writing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.search.teacher.model.entities.modules.writing.WritingModule;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.dto.modules.writing
 * Created by doston.rakhimov
 * Date: 22/10/24
 * Time: 17:45
 **/
@Getter
@Setter
public class WritingDto {
    private Long id;

    @NotNull
    private String content;

    @JsonProperty("task_one")
    private boolean taskOne;

    @JsonProperty("image_url")
    private String imageUrl;

    public WritingDto() {
    }

    public WritingDto(WritingModule module) {
        this.id = module.getId();
        this.content = module.getContent();
        this.taskOne = module.isTaskOne();
        this.imageUrl = module.getImageUrl();
    }
}
