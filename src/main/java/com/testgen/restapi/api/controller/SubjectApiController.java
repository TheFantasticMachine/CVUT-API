package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.Subject;
import com.testgen.restapi.api.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/subject")
public class SubjectApiController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectApiController(SubjectService subjectService) { this.subjectService = subjectService; }

    @GetMapping("/by-subject-name")
    public ResponseEntity<Subject> getSubjectByName(@RequestParam String name) {
        Subject subject = subjectService.getSubjectByName(name);
        return ResponseEntity.ok(subject);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }
}
