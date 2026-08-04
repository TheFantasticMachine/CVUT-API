package com.testgen.restapi.core.managers;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.api.model.Question;
import com.testgen.restapi.api.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/CVUT";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();

        try (Connection connection = getConnection()) {
            // create the questions
            String sql = "select * from questions where status='approved'";
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

    public List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM subjects";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubjectID(rs.getInt("subjectID"));
                subject.setSubjectName(rs.getString("name"));
                list.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));

                list.add(category
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
