package com.testgen.restapi.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRequest {

    private String title;
    private String subject;
    private String variant;
    private List<QuestionData> questions = new ArrayList<>();

    public TestRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getVariant() { return variant; }
    public void setVariant(String variant) { this.variant = variant; }

    public List<QuestionData> getQuestions() { return questions; }
    public void setQuestions(List<QuestionData> questions) { this.questions = questions; }

    // Nested Question DTO
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QuestionData {
        private Integer questionID;
        private String assignment;
        private Integer categoryID;
        private Integer difficulty;
        private String status;
        private List<AnswerData> answers = new ArrayList<>();

        public QuestionData() {}

        public Integer getQuestionID() { return questionID; }
        public void setQuestionID(Integer questionID) { this.questionID = questionID; }

        public String getAssignment() { return assignment; }
        public void setAssignment(String assignment) { this.assignment = assignment; }

        public Integer getCategoryID() { return categoryID; }
        public void setCategoryID(Integer categoryID) { this.categoryID = categoryID; }

        public Integer getDifficulty() { return difficulty; }
        public void setDifficulty(Integer difficulty) { this.difficulty = difficulty; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public List<AnswerData> getAnswers() { return answers; }
        public void setAnswers(List<AnswerData> answers) { this.answers = answers; }
    }

    // Nested Answer DTO (Handles both "correct" and "isCorrect")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnswerData {
        private Integer answerID;
        private String answerText;

        @JsonProperty("correct")
        private boolean correct;

        public AnswerData() {}

        public Integer getAnswerID() { return answerID; }
        public void setAnswerID(Integer answerID) { this.answerID = answerID; }

        public String getAnswerText() { return answerText; }
        public void setAnswerText(String answerText) { this.answerText = answerText; }

        public boolean isCorrect() { return correct; }
        public boolean getCorrect() { return correct; }

        public void setCorrect(boolean correct) { this.correct = correct; }

        @JsonProperty("isCorrect")
        public void setIsCorrect(boolean isCorrect) { this.correct = isCorrect; }
    }
}