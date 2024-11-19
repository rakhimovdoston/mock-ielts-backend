package com.search.teacher.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Package com.search.teacher.dto.request
 * Created by doston.rakhimov
 * Date: 16/11/24
 * Time: 11:23
 **/
@Getter
@Setter
public class DeleteItem {
    private List<Long> ids;
}
