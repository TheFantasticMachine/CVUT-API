package com.testgen.restapi.core;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.api.model.Subject;
import com.testgen.restapi.api.repo.CategoryRepo;
import com.testgen.restapi.api.repo.SubjectRepo;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Globals implements CommandLineRunner {

    public static List<Subject> subjects = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();

    @Override
    public void run(String... args) {
        refreshGlobals();
    }

    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    // Call this whenever a new subject/category is added to update memory without restarting the app
    public void refreshGlobals() {
        subjects = subjectRepo.findAll();
        categories = categoryRepo.findAll();

        System.out.println("[Globals] Successfully loaded " + subjects.size() + " subjects and " + categories.size() + " categories.");
    }
}
