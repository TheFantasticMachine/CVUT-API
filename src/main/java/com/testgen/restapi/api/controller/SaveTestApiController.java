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

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("/summaries-by-user-id")
    public ResponseEntity<?> getSummariesByUserID (@RequestParam (required = false, defaultValue = "1") Integer userId) {
        List<?> summaries = savedTestService.getTestSummariesByUserId(userId);
        if (summaries == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "fail", "error", "no test found"));
        }
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/test-by-id")
    public ResponseEntity<?> getTestById (@RequestParam (required = false, defaultValue = "1") Integer testId) {
        Optional<SavedTest> test = savedTestService.getTestByID(testId);

        if (test.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "fail", "error", "test with searched id not found"));
        }

        return ResponseEntity.ok(test.get());
    }
}
