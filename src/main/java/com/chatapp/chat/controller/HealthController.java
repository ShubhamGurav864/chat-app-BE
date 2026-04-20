package com.chatapp.chat.controller;

import com.chatapp.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {
    
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    
    @GetMapping("/health")
    public String health() {
        try {
            String dbName = mongoTemplate.getDb().getName();
            long userCount = userRepository.count();
            return "✅ MongoDB Connected! Database: " + dbName + ", Users: " + userCount;
        } catch (Exception e) {
            return "❌ MongoDB Error: " + e.getMessage();
        }
    }
}