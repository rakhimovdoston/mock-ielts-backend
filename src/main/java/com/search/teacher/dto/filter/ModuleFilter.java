package com.search.teacher.dto.filter;

import com.search.teacher.model.enums.ModuleType;
import lombok.Getter;
import lombok.Setter;

/**
 * Package com.search.teacher.dto.filter
 * Created by doston.rakhimov
 * Date: 25/06/24
 * Time: 10:04
 **/
@Getter
@Setter
public class ModuleFilter extends PageFilter {
    private String type;
}
