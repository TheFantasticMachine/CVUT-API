package com.testgen.restapi.api.model;

import java.util.ArrayList;

public class Subject {

    private String subjectName;
    private int subjectID;
    private ArrayList<Category> categories;

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

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
