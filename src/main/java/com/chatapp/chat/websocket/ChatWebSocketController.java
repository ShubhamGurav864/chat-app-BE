package com.chatapp.chat.websocket;

import com.chatapp.chat.model.Message;
import com.chatapp.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(Message message) {

        // Save message
        Message savedMessage = messageService.saveMessage(message);

        // Send ONLY to receiver
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(), // username
                "/queue/messages",
                savedMessage
        );
    }
}