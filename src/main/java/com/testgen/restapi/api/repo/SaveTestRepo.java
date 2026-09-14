package com.testgen.restapi.api.repo;

import com.testgen.restapi.api.model.SavedTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaveTestRepo extends JpaRepository<SavedTest, Integer> {

    int countByUserId(Integer userId);

    List<SavedTest> findAllByUserId(Integer userId);

    Optional<SavedTest> getSavedTestByTestId(Integer testId);
}
