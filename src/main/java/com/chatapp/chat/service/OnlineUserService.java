package com.chatapp.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OnlineUserService {

    private final StringRedisTemplate redisTemplate;

    private static final String KEY = "ONLINE_USERS";

    public void userOnline(String username) {
        redisTemplate.opsForSet().add(KEY, username);
    }

    public void userOffline(String username) {
        redisTemplate.opsForSet().remove(KEY, username);
    }

    public Set<String> getOnlineUsers() {
        return redisTemplate.opsForSet().members(KEY);
    }

    public boolean isOnline(String username) {
        return redisTemplate.opsForSet().isMember(KEY, username);
    }
}