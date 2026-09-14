package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.SavedTest;
import com.testgen.restapi.api.model.User;
import com.testgen.restapi.api.repo.SaveTestRepo;
import com.testgen.restapi.api.service.SavedTestService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/test_save")
public class SaveTestApiController {

    private record NewTestRequest(
            String title,
            String subject_name
    ){}

    private final SavedTestService savedTestService;

    @Autowired
    public SaveTestApiController(SavedTestService savedTestService) {
        this.savedTestService = savedTestService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createNewTest(@RequestBody NewTestRequest request, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if  (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "fail", "error", "user not logged in"));
        }

        try {
            SavedTest createdTest = savedTestService.createNewTest(user.getId(), request.title, request.subject_name);
            return ResponseEntity.ok(createdTest);
        }
        catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "fail", "error", exception.getMessage()));
        }
    }
}
