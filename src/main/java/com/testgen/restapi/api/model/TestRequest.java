package com.testgen.restapi.api.model;

import java.util.List;

public class TestRequest {
    private String variant;
    private String subject;
    private String title;
    private List<Question> questions;

    // 🔑 REQUIRED by Jackson for JSON deserialization
    public TestRequest() {}

    public TestRequest(String testName, String subject, String title, List<Question> questions) {
        this.variant = testName;
        this.subject = subject;
        this.title = title;
        this.questions = questions;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
}