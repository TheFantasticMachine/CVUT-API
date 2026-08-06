package com.testgen.restapi.api.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionRequest {

    private int categoryID;
    private String assignment;
    private int correctIndex;
    private List<String> answers;

    public QuestionRequest() {}

    public QuestionRequest(int categoryID, String assignment, int correctIndex, List<String> answers) {
        this.categoryID = categoryID;
        this.assignment = assignment;
        this.correctIndex = correctIndex;
        this.answers = answers;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
