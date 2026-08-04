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
    }

}
