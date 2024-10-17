package com.search.teacher.dto.request;

import lombok.Data;

/**
 * Package com.search.teacher.dto.request
 * Created by doston.rakhimov
 * Date: 17/10/24
 * Time: 17:42
 **/
@Data
public class UserUpdate {
    private Long id;
    private String firstname;
    private String lastname;
}
