package com.testgen.restapi.api.controller;


import com.testgen.restapi.api.model.QuestionRequest;
import com.testgen.restapi.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserApiController {
    @GetMapping ("/get")
    public User getUser (@RequestParam String email, @RequestParam String password) {
        return null;
    }
}
