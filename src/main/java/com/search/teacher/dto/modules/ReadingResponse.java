package com.search.teacher.dto.modules;

import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import com.search.teacher.utils.Utils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Package com.search.teacher.dto.modules
 * Created by doston.rakhimov
 * Date: 25/06/24
 * Time: 12:40
 **/
@Getter
@Setter
public class ReadingResponse {
    private Long id;
    private String title;
    private String description;
    private String passage;
    private List<String> headings;
    private boolean list;
    private List<ReadingQuestionResponse> question;

    public ReadingResponse(ReadingPassage passage) {
        this.id = passage.getId();
        this.title = passage.getTitle();
        this.description = passage.getDescription();
        this.passage = passage.getContent();
        this.list = passage.isList();
        this.headings = passage.isList() ? Utils.getHeadingList(passage.getCount()) : new ArrayList<>();
    }
}
