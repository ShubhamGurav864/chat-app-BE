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

    // ✅ REGISTER
    public User register(User user) {

        if (user.getUsername() == null || user.getPassword() == null) {
            throw new RuntimeException("Username and password required");
        }

        Optional<User> existing = userRepository.findByUsername(user.getUsername());

        if (existing.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        return userRepository.save(user);
    }

    // ✅ ADD THIS METHOD (MISSING ONE)
    public Optional<User> login(String username, String password) {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }

        return Optional.empty();
    }
}