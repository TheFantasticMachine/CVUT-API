package com.testgen.restapi.api.repo;

import com.testgen.restapi.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {

    List<Category> findBySubjectID(Integer subjectID);

    Category findByCategoryName(String categoryName);
}
