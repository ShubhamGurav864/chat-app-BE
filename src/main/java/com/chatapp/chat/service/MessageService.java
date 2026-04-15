package com.chatapp.chat.service;

import com.chatapp.chat.model.Message;
import com.chatapp.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getChat(String sender, String receiver) {
        return messageRepository.findBySenderAndReceiver(sender, receiver);
    }
}