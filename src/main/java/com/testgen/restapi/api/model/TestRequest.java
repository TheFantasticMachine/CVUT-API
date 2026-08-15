package com.testgen.restapi.api.model;

import java.util.List;

public class TestRequest {
    private String variant;
    private List<Question> questions;

    // 🔑 REQUIRED by Jackson for JSON deserialization
    public TestRequest() {}

    public TestRequest(String testName, List<Question> questions) {
        this.variant = testName;
        this.questions = questions;
    }

    public String getTestVariant() { return variant; }
    public void setTestVariant(String variant) { this.variant = variant; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
}