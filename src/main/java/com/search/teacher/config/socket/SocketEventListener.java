package com.search.teacher.config.socket;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.SocketEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocketEventListener {

    @EventListener
    public void handleSocketEvent(SocketEvent event) {

    }
}
