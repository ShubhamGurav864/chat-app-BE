package com.chatapp.chat.controller;

import com.chatapp.chat.model.Message;
import com.chatapp.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        return messageService.saveMessage(message);
    }

    @GetMapping
    public List<Message> getChat(
            @RequestParam String sender,
            @RequestParam String receiver) {
        return messageService.getChat(sender, receiver);
    }
}