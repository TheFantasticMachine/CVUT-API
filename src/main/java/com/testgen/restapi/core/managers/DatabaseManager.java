package com.testgen.restapi.core.managers;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.api.model.Question;
import com.testgen.restapi.api.model.QuestionRequest;
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

    public static List<Subject> getAllSubjects() {
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

    public static List<Category> getAllCategories() {
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

    private void insertQuestionAnswers(Connection conn, int questionId, List<String> answers) throws SQLException {
        String sql = "INSERT INTO answers (question_id, answer_text) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (String answer : answers) {
                stmt.setInt(1, questionId);
                stmt.setString(2, answer);
                stmt.addBatch(); // Batch insert for performance
            }
            stmt.executeBatch();
        }
    }

    public Question createQuestion(QuestionRequest request) {
        // 1. SQL query matching your table column names exactly
        String sql = "INSERT INTO questions (questionText, correctAnswer, otherAnswer, categoryID, difficulty) VALUES (?, ?, ?, ?, ?)";

        // 2. Extract correct answer text and collect distractor answers
        List<String> allAnswers = request.getAnswers();
        int correctIndex = request.getCorrectAnswerIndex();

        String correctAnswerText = "";
        List<String> wrongAnswersList = new ArrayList<>();

        for (int i = 0; i < allAnswers.size(); i++) {
            if (i == correctIndex) {
                correctAnswerText = allAnswers.get(i);
            } else {
                wrongAnswersList.add(allAnswers.get(i));
            }
        }

        // Join incorrect choices with pipe delimiter '|' (e.g. "6|12|20")
        String otherAnswerText = String.join("|", wrongAnswersList);

        try (Connection conn = getConnection(); // Use your connection method
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 3. Bind parameters
            stmt.setString(1, request.getAssignment());
            stmt.setString(2, correctAnswerText);
            stmt.setString(3, otherAnswerText);
            stmt.setInt(4, request.getCategoryID());
            stmt.setInt(5, request.getDifficulty());

            stmt.executeUpdate();

            // 4. Retrieve auto-incremented questionID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int newQuestionId = rs.getInt(1);

                    return new Question(
                            newQuestionId,
                            request.getCategoryID(),
                            request.getCorrectAnswerIndex(),
                            request.getAssignment(),
                            request.getAnswers()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB Error] Failed to insert question: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
