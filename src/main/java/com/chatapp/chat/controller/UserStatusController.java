package com.chatapp.chat.controller;

import com.chatapp.chat.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserStatusController {

    private final OnlineUserService onlineUserService;

    @GetMapping("/online")
    public Set<String> getOnlineUsers() {
        return onlineUserService.getOnlineUsers();
    }

    @GetMapping("/status/{username}")
    public String isOnline(@PathVariable String username) {
        return onlineUserService.isOnline(username) ? "ONLINE" : "OFFLINE";
    }
}