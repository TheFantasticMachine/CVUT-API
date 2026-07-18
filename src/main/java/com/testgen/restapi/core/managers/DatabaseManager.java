package com.testgen.restapi.core.managers;

import com.testgen.restapi.api.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/CVUT";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // create the questions
            String sql = "select * from questions";
            Statement statement = connection.createStatement();
            ResultSet raw = statement.executeQuery(sql);

            while(raw.next()) {
                // loop and create
                int id = raw.getInt("questionID");
                String assignment =  raw.getString("questionText");
                String correct = raw.getString("correctAnswer");
                String otherAsString = raw.getString("otherAnswer");
                int categoryId = raw.getInt("categoryID");

                List<String> other = new ArrayList<>();
                other.addAll(Arrays.asList(otherAsString.split("\\|")));
                List<String> answers = new ArrayList<>();
                answers.add(correct);
                answers.addAll(other);

                Collections.shuffle(answers);


                Question question = new Question(id, categoryId, answers.indexOf(correct), assignment, answers);
                questions.add(question);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return questions;
    }
}
