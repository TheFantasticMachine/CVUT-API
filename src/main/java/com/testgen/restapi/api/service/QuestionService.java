// src/main/java/com/testgen/restapi/api/service/QuestionService.java
package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.api.model.Question;
import com.testgen.restapi.api.model.Subject;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private final DatabaseManager dbManager = new DatabaseManager();

    public List<Question> getAllQuestions() { return DatabaseManager.getAllQuestions(); }

    public Question getQuestionById(int id) {
        List<Question> questions = this.getAllQuestions();

        Question result = null;

        for (Question question: questions) {
            if (question.getQuestionID() == id) {
                result = question;
            }
        }

        return result;
    }

    public List<Question> getQuestionsBySubjectName(String subjectName) {
        // get sub
        Subject subject = null;
        List<Subject> subjects = dbManager.getAllSubjects();

        for (Subject sub: subjects) {
            if (sub.getSubjectName().equals(subjectName)) {
                subject = sub;
            }
        }

        // get all categories
        List<Category> categories = new ArrayList<>();

        for (Category category: dbManager.getAllCategories()) {
            assert subject != null;
            if (category.getSubjectID() == subject.getSubjectID()) {
                categories.add(category);
            }
        }

        // now filter questions
        List<Question> questions = new ArrayList<>();

        for (Question question: this.getAllQuestions()) {
            for (Category category: categories) {
                if (question.getCategoryID() == category.getCategoryID()) {
                    questions.add(question);
                }
            }
        }

        return questions;
    }

    public List<Question> getQuestionsBySubjectId (int subjectId) {
        // get all categories
        List<Category> categories = new ArrayList<>();

        for (Category category: dbManager.getAllCategories()) {
            if (category.getSubjectID() == subjectId) {
                categories.add(category);
            }
        }

        // now filter questions
        List<Question> questions = new ArrayList<>();

        for (Question question: this.getAllQuestions()) {
            for (Category category: categories) {
                if (question.getCategoryID() == category.getCategoryID()) {
                    questions.add(question);
                }
            }
        }

        return questions;
    }

    public List<Question> getQuestionsByCategoryName(String categoryName) {
        // now filter questions
        List<Question> questions = new ArrayList<>();
        Category category = null;

        for (Category cat : dbManager.getAllCategories()) {
            if (cat.getCategoryName().equals(categoryName)) {
                category = cat;
            }
        }

        for (Question question: this.getAllQuestions()) {
            assert category != null;
            if (question.getCategoryID() == category.getCategoryID()) {
                questions.add(question);
            }
        }

        return questions;
    }

    public List<Question> getQuestionsByCategoryId (int categoryId) {
        // now filter questions
        List<Question> questions = new ArrayList<>();

        for (Question question: this.getAllQuestions()) {
            if (question.getCategoryID() == categoryId) {
                questions.add(question);
            }
        }

        return questions;
    }
}