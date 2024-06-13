package com.search.teacher.dto.message;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChatRoomDto {
    private String userId;
    private String username;
    private String logo;
    private Date timestamp;
    private String message;
}
