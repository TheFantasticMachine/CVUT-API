package com.testgen.restapi.api.controller;


import com.testgen.restapi.api.model.QuestionRequest;
import com.testgen.restapi.api.model.User;
import com.testgen.restapi.api.repo.UserRepo;
import com.testgen.restapi.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    private UserService userService;

    @GetMapping ("/get")
    public User getUser (@RequestParam int id) {
        return null;
    }

    @GetMapping("/by-username")
    public ResponseEntity<User> getUserByUsername (@RequestParam String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/new")
    public User createNewUser(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        return userService.register(user);
    }

    @PostMapping("/verify")
    public User verifyUser(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        return userService.verify(user);
    }
}
