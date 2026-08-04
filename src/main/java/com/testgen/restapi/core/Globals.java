package com.testgen.restapi.core;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.api.model.Subject;

import java.util.ArrayList;
import java.util.HashMap;

public class Globals {

    static private HashMap<String, Subject> allSubjects;
    static private HashMap<String, Category> allCategories;

    public Globals() {}

    public HashMap getAllSubjects() {
        return allSubjects;
    }

    public HashMap getAllCategories() {
        return allCategories;
    }

    public void addToSubjects(Subject subject) {
        if (allSubjects.containsKey(subject.getSubjectName())) { return; }
        else {
            allSubjects.put(subject.getSubjectName(), subject);
        }
        return;
    }

    public void addToCategories(Category category) {
        if (allCategories.containsKey(category.getCategoryName())) { return; }
        else {
            allCategories.put(category.getCategoryName(), category);
        }
        return;
    }

    public void removeFromSubjects(Subject subject) {
        if (allSubjects.containsKey(subject.getSubjectName())) {
            allSubjects.remove(subject.getSubjectName(), subject);
        }
        return;
    }

    public void removeFromCategories(Category category) {
        if (allCategories.containsKey(category.getCategoryName() )) {
            allSubjects.remove(category.getCategoryName(), category);
        }
        return;
    }

}
