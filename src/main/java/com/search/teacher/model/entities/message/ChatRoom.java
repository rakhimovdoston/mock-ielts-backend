package com.search.teacher.model.entities.message;

import com.search.teacher.model.base.BaseEntity;
import com.search.teacher.model.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends BaseEntity {

    private String chatId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_user_id", referencedColumnName = "id")
    private User receiverUser; // receiver userId

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_user_id", referencedColumnName = "id")
    private User senderUser; //
}