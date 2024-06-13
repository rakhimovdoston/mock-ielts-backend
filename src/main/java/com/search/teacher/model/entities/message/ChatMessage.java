package com.search.teacher.model.entities.message;

import com.search.teacher.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
public class ChatMessage extends BaseEntity {
    private String chatId;
    private Long toUserId;
    private Long fromUserId;
    private String content;
    private Date timestamp;
    private MessageType messageType;
    private String fileUrl; // messageType is not text
}
