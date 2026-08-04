package com.testgen.restapi.ui.controller;

import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QuestionMakerController {

    @GetMapping("/question_maker")
    public String question_maker(Model model) {

        DatabaseManager databaseManager = new DatabaseManager();

        List subjects = new ArrayList<>();

        try {
            Connection connection = databaseManager.getConnection();
            String sql = "select categoryName from categories";
            Statement statement =  connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                subjects.add(result.getString("categoryName"));
            }
            model.addAttribute("subjects", subjects);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "question_maker";
    }
}
