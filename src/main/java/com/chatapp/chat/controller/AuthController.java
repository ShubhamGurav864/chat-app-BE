package com.chatapp.chat.controller;

import com.chatapp.chat.model.User;
import com.chatapp.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Optional<User> loggedIn = userService.login(user.getUsername(), user.getPassword());

        return loggedIn.isPresent() ? "SUCCESS" : "FAIL";
    }
}