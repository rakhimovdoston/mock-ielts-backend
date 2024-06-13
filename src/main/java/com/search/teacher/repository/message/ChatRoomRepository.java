package com.search.teacher.repository.message;

import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.message.ChatRoom;
import com.search.teacher.repository.ChatUserCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatUserCustomRepository {
    Page<ChatRoom> findAllByReceiverUserAndActiveIsTrueOrderByUpdatedDateDesc(User receiverUser, Pageable pageable);

    ChatRoom findByReceiverUserAndSenderUserAndActiveIsTrue(User receiverUser, User senderUser);
}
