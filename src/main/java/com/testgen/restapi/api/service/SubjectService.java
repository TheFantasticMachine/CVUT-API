package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.api.model.Subject;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    public List<Subject> getAllSubjects() { return DatabaseManager.getAllSubjects(); }

    public Subject getSubjectByName(String name) {
        for (Subject subject : this.getAllSubjects()) {
            if (subject.getSubjectName().equals(name)) {
                return subject;
            }
        }
        return null;
    }

    public static Subject getSubjectByCategoryId(int id) {
        for (Category category : DatabaseManager.getAllCategories()) {
            if (category.getCategoryID() == id) {
                for (Subject subject : DatabaseManager.getAllSubjects()) {
                    if (subject.getSubjectID() == category.getSubjectID()) {
                        return subject;
                    }
                }
            }
        }
        return null;
    }
}
