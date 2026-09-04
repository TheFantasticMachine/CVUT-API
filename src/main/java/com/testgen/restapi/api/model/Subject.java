package com.testgen.restapi.api.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Integer subjectID;
    @Column(name = "subject_name")
    private String subjectName;

    @OneToMany
    @JoinColumn(name = "subject_id")
    private List<Category> categories = new ArrayList<>();

    public Subject(String subjectName, int subjectID, ArrayList<Category> categories) {
        this.subjectName = subjectName;
        this.subjectID = subjectID;
        this.categories = categories;
    }

    public Subject() {}

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
