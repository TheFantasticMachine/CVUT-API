package com.testgen.restapi.api.model;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryID;
    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "subject_id")
    private Integer subjectID;

    @OneToMany
    @JoinColumn(name = "category_id")
    private ArrayList<Question> questions;

    public Category(String categoryName, int categoryID, int subjectID, ArrayList<Question> questions) {
        this.categoryName = categoryName;
        this.categoryID = categoryID;
        this.subjectID = subjectID;
        this.questions = questions;
    }

    public Category() {}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
