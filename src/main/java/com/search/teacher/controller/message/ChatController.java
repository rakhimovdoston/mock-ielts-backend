package com.search.teacher.controller.message;

import com.search.teacher.dto.message.ChatDto;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.message.ChatMessageService;
import com.search.teacher.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final SecurityUtils securityUtils;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/messages")
    public JResponse sendMessage(@Payload ChatDto message) {
        return chatMessageService.sendMessage(securityUtils.currentUser(), message);
    }
}
