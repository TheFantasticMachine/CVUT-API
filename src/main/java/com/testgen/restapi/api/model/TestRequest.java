package com.testgen.restapi.api.model;

import java.util.List;

public class TestRequest {
    private String testName;
    private int categoryId;
    private List<Integer> questionIds; // Or List<Question> if sending full objects

    // Default constructor needed for Jackson JSON parsing
    public TestRequest() {}

    public TestRequest(String testName, int categoryId, List<Integer> questionIds) {
        this.testName = testName;
        this.categoryId = categoryId;
        this.questionIds = questionIds;
    }

    // Getters and Setters
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public List<Integer> getQuestionIds() { return questionIds; }
    public void setQuestionIds(List<Integer> questionIds) { this.questionIds = questionIds; }
}