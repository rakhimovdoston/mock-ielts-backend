package com.search.teacher.dto.message;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChatDto {
    private Long toUserId;
    private String chatId;
    private String message;
    private Date timestamp;
}
