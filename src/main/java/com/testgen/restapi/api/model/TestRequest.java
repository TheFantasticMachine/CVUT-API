package com.testgen.restapi.api.model;

import java.util.List;

public class TestRequest {
    private String testName;
    private List<Question> questions;

    // 🔑 REQUIRED by Jackson for JSON deserialization
    public TestRequest() {}

    public TestRequest(String testName, List<Question> questions) {
        this.testName = testName;
        this.questions = questions;
    }

    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
}