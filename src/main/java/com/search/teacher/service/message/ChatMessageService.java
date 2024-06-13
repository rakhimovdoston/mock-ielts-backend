package com.search.teacher.service.message;

import com.amazonaws.services.securityhub.model.SortOrder;
import com.search.teacher.dto.filter.MessageFilter;
import com.search.teacher.dto.filter.PageFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.message.ChatDto;
import com.search.teacher.dto.message.ChatRoomDto;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.message.ChatMessage;
import com.search.teacher.model.entities.message.ChatRoom;
import com.search.teacher.model.enums.SortTypeEnum;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.message.ChatMessageRepository;
import com.search.teacher.repository.UserRepository;
import com.search.teacher.repository.message.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatUserRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserService chatUserService;

    public JResponse sendMessage(User user, ChatDto dto) {
        User userEntity = userRepository.findById(dto.getToUserId()).orElseThrow(() -> new NotfoundException("User Not Found"));

        String chatId = chatUserService.getChatId(user, userEntity);
        if (!chatId.equals(dto.getChatId())) {
            return JResponse.error(400, "Chat Id Not Matched");
        }

        ChatMessage message = new ChatMessage();
        message.setChatId(chatId);
        message.setToUserId(user.getId());
        message.setFromUserId(userEntity.getId());
        message.setContent(dto.getMessage());
        message.setTimestamp(new Date());
        chatMessageRepository.save(message);
        return JResponse.success();
    }

    public JResponse getLastMessage(User user, MessageFilter filter) {
        User userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new NotfoundException("User Not Found"));

        String chatId = chatUserService.getChatId(user, userEntity);
        if (chatId == null) {
            return JResponse.error(404, "Not Found");
        }
        Page<ChatMessage> messages = chatMessageRepository.findAllByActiveIsTrueAndChatId(chatId, filter.getPageable());

        if (messages.getContent().isEmpty()) return JResponse.error(404, "Not Found");

        PaginationResponse response = new PaginationResponse();
        response.setData(messages.getContent());
        response.setCurrentPage(filter.getPage());
        response.setCurrentSize(filter.getSize());
        response.setTotalPages(messages.getTotalPages());
        response.setTotalSizes(messages.getTotalElements());
        return JResponse.success(response);
    }

    public JResponse getUserAllConnections(User user, PageFilter filter) {
        Page<ChatRoom> users = chatUserRepository.findAllByReceiverUserAndActiveIsTrueOrderByUpdatedDateDesc(user, filter.getPageable());
        if (users.getContent().isEmpty()) {
            return JResponse.error(404, "Not Found");
        }

        List<ChatRoomDto> dtos = new ArrayList<>();
        for (ChatRoom chatRoom : users.getContent()) {
            ChatRoomDto dto = new ChatRoomDto();
            User senderUser = chatRoom.getSenderUser();
            dto.setUserId(String.valueOf(senderUser.getId()));
            dto.setUsername(senderUser.getFirstname() != null ? senderUser.getFirstname() : senderUser.getLastname());
            dto.setLogo(senderUser.getFirstname());
            dto.setTimestamp(chatRoom.getUpdatedDate());
            dtos.add(dto);
        }
        PaginationResponse response = new PaginationResponse();
        response.setCurrentPage(filter.getPage());
        response.setCurrentSize(filter.getSize());
        response.setTotalSizes(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
        response.setData(dtos);
        return JResponse.success(response);
    }
}
