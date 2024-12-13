package com.search.teacher.model.entities.modules.reading;

import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.model.entities.modules.reading
 * Created by doston.rakhimov
 * Date: 15/10/24
 * Time: 11:28
 **/
@Getter
@Setter
public class Form {
    private String text;
    private Integer order;
    private String answer;

    public Form() {
    }

    public Form(String text) {
        this.text = text;
    }


    public Form(String text, Integer order) {
        this.text = text;
        this.order = order;
    }
}
