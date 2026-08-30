package com.testgen.restapi.api.model;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    private String subjectName;
    private int subjectID;
    private List<Category> categories = new ArrayList<>();

    public Subject(String subjectName, int subjectID, ArrayList<Category> categories) {
        this.subjectName = subjectName;
        this.subjectID = subjectID;
        this.categories = categories;
    }

    public Subject() {}

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
