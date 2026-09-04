// src/main/java/com/testgen/restapi/api/service/QuestionService.java
package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.api.model.Question;
import com.testgen.restapi.api.model.Subject;
import com.testgen.restapi.api.repo.CategoryRepo;
import com.testgen.restapi.api.repo.QuestionRepo;
import com.testgen.restapi.api.repo.SubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    public List<Question> getAllQuestions() { return questionRepo.findAll(); }

    public Question getQuestionById(int id) {
        Optional<Question> question = questionRepo.findById(id);
        if (question.isPresent()) {
            return question.get();
        }
        return null;
    }

    public List<Question> getQuestionsBySubjectName(String subjectName) {
        // get sub
        Subject subject = subjectRepo.findBySubjectName(subjectName);

        return getQuestionsBySubjectId(subject.getSubjectID());
    }

    public List<Question> getQuestionsBySubjectId (int subjectId) {
        List<Question> questions = new ArrayList<>();

        for (Category category : categoryRepo.findBySubjectID(subjectId)) {
            questions.addAll(questionRepo.findByCategoryIDAndStatus(category.getCategoryID(), "APPROVED"));
        }

        return questions;
    }

    public List<Question> getQuestionsByCategoryName(String categoryName) {
        // now filter questions
        Category category = categoryRepo.findByCategoryName(categoryName);

        return questionRepo.findByCategoryIDAndStatus(category.getCategoryID(), "APPROVED");
    }

    public List<Question> getQuestionsByCategoryId (int categoryId) {
        // now filter questions
        Optional<Category> category = categoryRepo.findById(categoryId);

        return category.map(value -> questionRepo.findByCategoryIDAndStatus(value.getCategoryID(), "APPROVED")).orElse(null);
    }
}