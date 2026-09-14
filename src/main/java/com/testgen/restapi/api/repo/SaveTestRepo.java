package com.testgen.restapi.api.repo;

import com.testgen.restapi.api.model.SavedTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveTestRepo extends JpaRepository<SavedTest, Integer> {
}
