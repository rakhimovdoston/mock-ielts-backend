package com.search.teacher.repository;

import com.search.teacher.dto.filter.PageFilter;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.message.ChatRoom;

import java.util.List;

public interface ChatUserCustomRepository {
    int countUserChatRooms(User user, PageFilter pageFilter);

    List<ChatRoom> getAllUserChatRooms(User user, PageFilter filter);
}
