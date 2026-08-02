package com.testgen.restapi.api.model;

import java.util.List;

public class Question {
    private int questionID;
    private int categoryID;
    private int correctAnswerIndex;
    private String assignment;
    private List<String> answers;

    // 🔑 REQUIRED by Jackson for nested array deserialization
    public Question() {}

    public Question(int questionID, int categoryID, int correctAnswerIndex, String assignment, List<String> answers) {
        this.questionID = questionID;
        this.categoryID = categoryID;
        this.correctAnswerIndex = correctAnswerIndex;
        this.assignment = assignment;
        this.answers = answers;
    }

    public int getQuestionID() { return questionID; }
    public void setQuestionID(int questionID) { this.questionID = questionID; }

    public int getCategoryID() { return categoryID; }
    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }

    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public void setCorrectAnswerIndex(int correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }

    public String getAssignment() { return assignment; }
    public void setAssignment(String assignment) { this.assignment = assignment; }

    public List<String> getAnswers() { return answers; }
    public void setAnswers(List<String> answers) { this.answers = answers; }
}