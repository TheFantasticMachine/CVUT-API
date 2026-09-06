package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.User;
import com.testgen.restapi.api.repo.UserRepo;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User registerUser(String username, String rawPassword) {
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken.");
        }

        String salt = BCrypt.gensalt(10);
        String hashed = BCrypt.hashpw(rawPassword, salt);

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashed);
        user.setRole("TEACHER");

        return userRepo.save(user);
    }

    public Optional<User> authenticate(String username, String rawPassword) {
        if (username == null || rawPassword == null) {
            return Optional.empty();
        }

        Optional<User> userOpt = userRepo.findByUsername(username.trim());
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        // Check BCrypt hash match
        if (BCrypt.checkpw(rawPassword.trim(), user.getPassword())) {
            return Optional.of(user);
        }

        return Optional.empty();
    }


    public User findByUsername(String username) {
        Optional<User> user = userRepo.findByUsername(username);
        return user.orElse(null);
    }
}
