package com.testgen.restapi;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.api.model.Subject;
import com.testgen.restapi.core.Globals;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@SpringBootApplication
public class RestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);

        // load globals
        DatabaseManager databaseManager = new DatabaseManager();
        Globals globals = new Globals();
        String sql = "";

        try (Connection connection = databaseManager.getConnection()) {
            sql = "select * from categoriers";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Category category = new Category();

                category.setCategoryName(resultSet.getString("categoryName"));
                category.setCategoryID(resultSet.getInt("categoryID"));
                category.setSubjectID(resultSet.getInt("subjectID"));

                globals.addToCategories(category);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = databaseManager.getConnection()) {
            sql = "select * from subjects";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Subject subject = new Subject();

                subject.setSubjectName(resultSet.getString("name"));
                subject.setSubjectID(resultSet.getInt("subjectID"));

                ArrayList<Category> categories = new ArrayList<>();

                globals.getAllCategories().forEach( (k,  category) -> {
                    if (subject.getSubjectID() == category.getSubjectID) {
                        categories.add((Category) category);
                    }
                });

            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
