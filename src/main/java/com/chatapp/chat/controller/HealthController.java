package com.chatapp.chat.controller;

import com.chatapp.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    
    @Autowired(required = false)  // Make Redis optional
    private RedisTemplate<String, Object> redisTemplate;
    
    public HealthController(MongoTemplate mongoTemplate, UserRepository userRepository) {
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;
    }
    
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
        if (redisTemplate != null) {
            try {
                redisTemplate.opsForValue().set("health-check", "OK");
                String redisCheck = (String) redisTemplate.opsForValue().get("health-check");
                status.append("✅ Redis Connected! Test value: ")
                      .append(redisCheck);
            } catch (Exception e) {
                status.append("❌ Redis Error: ")
                      .append(e.getMessage());
            }
        } else {
            status.append("⚠️ Redis not configured");
        }
        
        return status.toString();
    }
}