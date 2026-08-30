package com.testgen.restapi.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.testgen.restapi.api.service.CategoryService;
import com.testgen.restapi.api.service.SubjectService;
import com.testgen.restapi.core.Globals;
import com.testgen.restapi.core.managers.DatabaseManager;

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

    public String getSubjectName() {
        DatabaseManager databaseManager = new DatabaseManager();
        List<Subject> subjects = databaseManager.getAllSubjects();
        List<Category> categories = databaseManager.getAllCategories();

        Category questionCategory = null;

        for (Category category: categories) {
            if (category.getCategoryID() == this.getCategoryID()) {
                questionCategory = category;
            }
        }

        Subject questionSubject = null;

        for (Subject subject: subjects) {
            if (subject.getCategories().contains(questionCategory)) {
                questionSubject = subject;
            }
        }

        if (questionSubject == null) {
            return "Unassigned"; // 👈 Prevents the NullPointerException
        }

        return questionSubject.getSubjectName();
    }

    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public void setCorrectAnswerIndex(int correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }

    public String getAssignment() { return assignment; }
    public void setAssignment(String assignment) { this.assignment = assignment; }

    public List<String> getAnswers() { return answers; }
    public void setAnswers(List<String> answers) { this.answers = answers; }

    // --- Helper Methods (Add @JsonIgnore and null checks) ---

    @JsonIgnore
    public Subject getSubject() {
        // Safe lookup: return null if Globals or Category is missing
        Category category = CategoryService.getCategoryById(this.categoryID);
        if (category == null) return null;
        return SubjectService.getSubjectByCategoryId(category.getSubjectID());
    }
}