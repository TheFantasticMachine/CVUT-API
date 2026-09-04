package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.Subject;
import com.testgen.restapi.api.repo.SubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepo subjectRepo;

    public List<Subject> getAllSubjects() { return subjectRepo.findAll(); }

    public Subject getSubjectByName(String name) {
        return subjectRepo.findBySubjectName(name);
    }
}
