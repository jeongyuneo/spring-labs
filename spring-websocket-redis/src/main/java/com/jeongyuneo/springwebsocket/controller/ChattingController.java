package com.jeongyuneo.springwebsocket.controller;

import com.jeongyuneo.springwebsocket.dto.Message;
import com.jeongyuneo.springwebsocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChattingController {

    private final ChattingService chattingService;

    @MessageMapping("/chattings/rooms/messages")
    public void send(Message message) {
        chattingService.send(message);
    }
}
