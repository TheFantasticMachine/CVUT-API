package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.User;
import com.testgen.restapi.api.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User verify(User user) {
        User compare = findByUsername(user.getUsername());
        if (encoder.matches (user.getPassword(), compare.getPassword())) {
            return compare;
        }
        return null;
    }

    public User findByUsername(String username) {
        Optional<User> user = userRepo.findByUsername(username);
        return user.orElse(null);
    }
}
