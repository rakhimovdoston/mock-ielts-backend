package com.search.teacher.service.message;

import com.search.teacher.dto.message.AddUserDto;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.enums.IStatus;
import com.search.teacher.model.response.JResponse;

public interface ChatUserService {
    JResponse addUser(User user, AddUserDto userDto);

    String getChatId(User senderUser, User receiverUser);

    JResponse userOnline(User user, IStatus status);
}
