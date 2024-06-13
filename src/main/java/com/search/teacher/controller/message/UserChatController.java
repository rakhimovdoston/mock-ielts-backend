package com.search.teacher.controller.message;

import com.search.teacher.dto.message.AddUserDto;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.message.ChatUserService;
import com.search.teacher.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserChatController {

    private final ChatUserService chatUserService;
    private final SecurityUtils securityUtils;

    @MessageMapping("/user.addUser")
    @SendTo("/topic/messages")
    public JResponse addUser(@Payload AddUserDto userDto) {
        return chatUserService.addUser(securityUtils.currentUser(), userDto);
    }
}
