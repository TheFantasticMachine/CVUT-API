package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.Subject;
import com.testgen.restapi.api.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/subject")
public class SubjectApiController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectApiController(SubjectService subjectService) { this.subjectService = subjectService; }

    @GetMapping("/all")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }
}
