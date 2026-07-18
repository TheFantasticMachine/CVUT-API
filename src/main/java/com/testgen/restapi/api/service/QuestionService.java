package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.Question;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionService {

    private List<Question> questionList;

    List<String> getAnswers() {
        List<String> answers = new ArrayList<>();
        answers.add("correct");
        answers.add("wrong 1");
        answers.add("wrong 2");
        answers.add("wrong 3");

        Collections.shuffle(answers);

        return answers;
    }

    public QuestionService() {
        questionList = new ArrayList<>();
        questionList.addAll(DatabaseManager.getAllQuestions());

//        List<String> answersTemp = getAnswers();
//        Question question1 = new Question(1, 1, answersTemp.indexOf("correct"), "question A", answersTemp);
//        answersTemp = getAnswers();
//        Question question2 = new Question(2, 2, answersTemp.indexOf("correct"), "question B", answersTemp);
//        answersTemp = getAnswers();
//        Question question3 = new Question(3, 3, answersTemp.indexOf("correct"), "question C", answersTemp);
//
//        answersTemp = getAnswers();
//        Question question4 = new Question(4, 1, answersTemp.indexOf("correct"), "question D", answersTemp);
//        answersTemp = getAnswers();
//        Question question5 = new Question(5, 2, answersTemp.indexOf("correct"), "question F", answersTemp);
//        answersTemp = getAnswers();
//        Question question6 = new Question(6, 3, answersTemp.indexOf("correct"), "question G", answersTemp);
//
//        answersTemp = getAnswers();
//        Question question7 = new Question(7, 1, answersTemp.indexOf("correct"), "question H", answersTemp);
//        answersTemp = getAnswers();
//        Question question8 = new Question(8, 2, answersTemp.indexOf("correct"), "question I", answersTemp);
//        answersTemp = getAnswers();
//        Question question9 = new Question(9, 3, answersTemp.indexOf("correct"), "question J", answersTemp);
//
//        questionList.addAll(Arrays.asList(question1, question2, question3, question4, question5, question6, question7, question8, question9));
    }

    public Optional<Question> getQuestion(Integer id) {
        Optional optional = Optional.empty();
        for (Question question: questionList) {
            if (id == question.getQuestionID()) {
                optional = Optional.of(question);
                return optional;
            }
        }
        return optional;
    }
}
