package com.chatapp.chat.controller;

import com.chatapp.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {
    
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @GetMapping("/health")
    public String health() {
        StringBuilder status = new StringBuilder();
        
        // MongoDB Check
        try {
            String dbName = mongoTemplate.getDb().getName();
            long userCount = userRepository.count();
            status.append("✅ MongoDB Connected! Database: ")
                  .append(dbName)
                  .append(", Users: ")
                  .append(userCount)
                  .append("\n");
        } catch (Exception e) {
            status.append("❌ MongoDB Error: ")
                  .append(e.getMessage())
                  .append("\n");
        }
        
        // Redis Check
        try {
            redisTemplate.opsForValue().set("health-check", "OK");
            String redisCheck = (String) redisTemplate.opsForValue().get("health-check");
            status.append("✅ Redis Connected! Test value: ")
                  .append(redisCheck);
        } catch (Exception e) {
            status.append("❌ Redis Error: ")
                  .append(e.getMessage());
        }
        
        return status.toString();
    }
}