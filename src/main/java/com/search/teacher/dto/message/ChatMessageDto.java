package com.search.teacher.dto.message;

import com.search.teacher.model.entities.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private String chatId;
    private String text;
    private Date timestamp;
    private MessageType type;
    private String fileUrl;
    private boolean isSender;
}
