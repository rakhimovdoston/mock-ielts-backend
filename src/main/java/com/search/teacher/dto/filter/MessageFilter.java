package com.search.teacher.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageFilter extends PageFilter {
    private Long toUserId;
}
