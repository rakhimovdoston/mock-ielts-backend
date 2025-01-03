package com.search.teacher.dto.modules;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Package com.search.teacher.dto.modules
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 15:01
 **/
@Getter
@Setter
public class ListeningDto {
    private Long id;
    private boolean active;
    private String title;
    private String difficulty;
    private boolean answers;
    private String audio;
    private List<String> types = new ArrayList<>();
}
