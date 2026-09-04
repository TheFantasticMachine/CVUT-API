// src/main/java/com/testgen/restapi/api/model/Question.java
package com.testgen.restapi.api.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions") // 👈 Explicitly binds to your SQL "questions" table
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id") // 👈 Binds to MySQL "question_id"
    private Integer questionID;

    @Column(name = "category_id")
    private Integer categoryID;

    @Column(name = "user_id")
    private Integer userID;

    @Column(name = "parent_id")
    private Integer parentID;

    @Column(name = "assignment", columnDefinition = "TEXT", nullable = false)
    private String assignment;

    @Column(name = "status")
    private String status = "PENDING";

    @Column(name = "review_message", columnDefinition = "TEXT")
    private String reviewMessage;

    @Column(name = "difficulty")
    private Integer difficulty = 1;

    // 👈 Relationship: One Question has Many Answers
    // cascade = CascadeType.ALL means saving/deleting a Question automatically saves/deletes its answers!
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Answer> answers = new ArrayList<>();

    public Question() {}

    // Helper method to add answers safely
    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setQuestion(this);
    }

    // Getters and Setters for all fields...
    public Integer getQuestionID() { return questionID; }
    public void setQuestionID(Integer questionID) { this.questionID = questionID; }
    public String getAssignment() { return assignment; }
    public void setAssignment(String assignment) { this.assignment = assignment; }
    public List<Answer> getAnswers() { return answers; }
    public void setAnswers(List<Answer> answers) { this.answers = answers; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getCategoryID() { return categoryID; }
    public void setCategoryID(Integer categoryID) { this.categoryID = categoryID; }
}