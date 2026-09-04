package com.testgen.restapi.api.repo;

import com.testgen.restapi.api.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Integer> {
    Subject findBySubjectName(String subjectName);
}
