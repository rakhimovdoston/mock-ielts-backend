package com.search.teacher.service.message;

import com.search.teacher.dto.message.AddUserDto;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.message.ChatRoom;
import com.search.teacher.model.enums.IStatus;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.UserRepository;
import com.search.teacher.repository.message.ChatMessageRepository;
import com.search.teacher.repository.message.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatUserServiceImpl implements ChatUserService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public JResponse addUser(User user, AddUserDto userDto) {
        User userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new NotfoundException("User not found"));

        String chatId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        ChatRoom senderUser = ChatRoom.builder().chatId(chatId).receiverUser(userEntity).senderUser(user).build();
        ChatRoom receiverUser = ChatRoom.builder().chatId(chatId).receiverUser(user).senderUser(userEntity).build();
        chatRoomRepository.save(senderUser);
        chatRoomRepository.save(receiverUser);
        return JResponse.success("User added successfully");
    }

    @Override
    public String getChatId(User senderUser, User receiverUser) {
        ChatRoom senderChatRoom = chatRoomRepository.findByReceiverUserAndSenderUserAndActiveIsTrue(senderUser, receiverUser);
        ChatRoom receiverRoom = chatRoomRepository.findByReceiverUserAndSenderUserAndActiveIsTrue(receiverUser, senderUser);
        if (senderChatRoom == null || receiverRoom == null) return null;

        if (senderChatRoom.getChatId().equals(receiverRoom.getChatId())) return senderChatRoom.getChatId();
        return null;
    }

    @Override
    public JResponse userOnline(User user, IStatus status) {
        user.setInternetStatus(status);
        userRepository.save(user);
        return JResponse.success();
    }
}
