package com.chatapp.chat.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp;
}
