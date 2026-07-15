package com.testgen.restapi.api.model;

import java.util.List;

public class Question {
    private int questionID, categoryID, correctIndex;
    private String assignment;
    private List<String> answers;

    public Question(int questionID, int categoryID, int correctIndex, String assignment, List<String> answers) {
        this.questionID = questionID;
        this.categoryID = categoryID;
        this.correctIndex = correctIndex;
        this.assignment = assignment;
        this.answers = answers;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
