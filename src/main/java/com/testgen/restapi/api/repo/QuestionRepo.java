package com.testgen.restapi.api.repo;

import com.testgen.restapi.api.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Integer> {

    List<Question> findByStatus(String status);

    List<Question> findByCategoryIDAndStatus(Integer categoryID, String status);
}
