package com.testgen.restapi.api.service;

import com.testgen.restapi.api.controller.UserApiController;
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

    public User create(UserApiController.UserRequest request) {
        // 1) check if possible
        if (request.password() == null || request.password().trim().isEmpty()) {
            throw new IllegalArgumentException("No password given");
        }
        if (request.username() == null || request.username().trim().isEmpty()) {
            throw new IllegalArgumentException("No username given");
        }
        if (userRepo.existsByUsername(request.username().trim())) {
            throw new IllegalArgumentException("Username taken");
        }

        // 2) create hash password
        String passwordHash = BCrypt.hashpw(request.password(), BCrypt.gensalt(10));

        // 3) create user and add to db
        User user = new User();
        user.setPassword(passwordHash);
        user.setUsername(request.username().trim());

        // 4) return a new user
        return userRepo.save(user);
    }

    public Optional<User> authenticate(UserApiController.UserRequest request) {
        // check if data is present
        if (request.password() == null || request.password().trim().isEmpty()) {
            throw new IllegalArgumentException("No password given");
        }
        if (request.username() == null || request.username().trim().isEmpty()) {
            throw new IllegalArgumentException("No username given");
        }

        Optional<User> optionalUser = userRepo.findByUsername(request.username());
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        if (BCrypt.checkpw(request.password(), user.getPassword())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }


    public User findByUsername(String username) {
        Optional<User> user = userRepo.findByUsername(username);
        return user.orElse(null);
    }
}
