package com.testgen.restapi.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryID;
    @Column(name = "category_name", nullable = false)
    private String categoryName;
    @Column(name = "subject_id", nullable = false)
    private Integer subjectID;

    public Category(String categoryName, int categoryID, int subjectID) {
        this.categoryName = categoryName;
        this.categoryID = categoryID;
        this.subjectID = subjectID;
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
}
