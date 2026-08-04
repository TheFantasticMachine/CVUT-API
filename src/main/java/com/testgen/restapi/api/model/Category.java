package com.testgen.restapi.api.model;

import java.util.ArrayList;

public class Category {

    private String categoryName;
    private int categoryID;
    private int subjectID;
    private ArrayList<Question> questions;

    public Category(String categoryName, int categoryID, int subjectID, ArrayList<Question> questions) {
        this.categoryName = categoryName;
        this.categoryID = categoryID;
        this.subjectID = subjectID;
        this.questions = questions;
    }

    public Category() {}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
