package com.chatapp.chat.service;

import com.chatapp.chat.model.User;
import com.chatapp.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

   public User register(User user) {

    // ✅ Validate input
    if (user.getUsername() == null || user.getUsername().isEmpty()) {
        throw new RuntimeException("Username is required");
    }

    if (user.getPassword() == null || user.getPassword().isEmpty()) {
        throw new RuntimeException("Password is required");
    }

    // ✅ Check duplicate user
    Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

    if (existingUser.isPresent()) {
        throw new RuntimeException("User already exists");
    }

    // ✅ Save user
    return userRepository.save(user);
}
}