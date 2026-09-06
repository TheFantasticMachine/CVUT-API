package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.User;
import com.testgen.restapi.api.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    private UserService userService;

    public record UserRequest(
            String username,
            String password
    ) {}

    public record AuthResponse(
            String status,
            String msg,
            String username,
            String role,
            Integer id
    ) {}

    @GetMapping ("/get")
    public User getUser (@RequestParam int id) {
        return null;
    }

    @GetMapping("/by-username")
    public ResponseEntity<User> getUserByUsername (@RequestParam String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody UserRequest request, HttpSession session) {
        try {
            Optional<User> authenticated = userService.authenticate(request);

            if (authenticated.isPresent()) {
                session.setAttribute("currentUser", authenticated.get());
                return ResponseEntity.ok(authenticated.get());
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Invalid username or password"
            ));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout (HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestParam UserRequest request, HttpSession session) {
        try {
            // make sure user can exist + create user if possible
            userService.create(request);

            // call login to set the user as active
            return login(request, session);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
