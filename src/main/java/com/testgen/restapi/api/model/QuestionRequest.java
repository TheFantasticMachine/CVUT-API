package com.testgen.restapi.api.model;

import com.testgen.restapi.core.managers.DatabaseManager;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class QuestionRequest {
    private String assignment;
    private int categoryID;
    private int difficulty;
    private int correctAnswerIndex;
    private List<String> answers;

    // Zero-argument constructor required for Jackson
    public QuestionRequest() {}

    public String getAssignment() { return assignment; }
    public void setAssignment(String assignment) { this.assignment = assignment; }

    public int getCategoryID() { return categoryID; }
    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public void setCorrectAnswerIndex(int correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }

    public List<String> getAnswers() { return answers; }
    public void setAnswers(List<String> answers) { this.answers = answers; }
}