package com.search.teacher.repository.impl;

import com.search.teacher.dto.filter.PageFilter;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.message.ChatRoom;
import com.search.teacher.repository.ChatUserCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatUserCustomRepositoryImpl implements ChatUserCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int countUserChatRooms(User user, PageFilter pageFilter) {
        String query = "select count(id) from ChatRoom where active is true and receiverUser.id = " + user.getId();
        return entityManager
                .createQuery(query, Integer.class).getFirstResult();
    }

    @Override
    public List<ChatRoom> getAllUserChatRooms(User user, PageFilter filter) {

        String query = "select ch from ChatRoom ch where receiverUser.id = " + user.getId() +
                " and active is true " +
                " order by " + filter.getOrderBy() + " desc";
        return entityManager
                .createQuery(query, ChatRoom.class)
                .getResultList();
    }
}
