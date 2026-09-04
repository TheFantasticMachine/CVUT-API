package com.testgen.restapi.api.repo;

import com.testgen.restapi.api.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepo extends JpaRepository<Answer, Integer> {
}
