package com.testgen.restapi.api.repo;

import com.testgen.restapi.api.model.SavedTest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaveTestRepo extends JpaRepository<SavedTest, Integer> {

    int countByUserId(Integer userId);

    List<SavedTest> findAllByUserId(Integer userId);

    Optional<SavedTest> getSavedTestByTestId(Integer testId);

    @Modifying
    @Transactional
    @Query("UPDATE SavedTest s SET s.testConfig = :config, s.testData = :data WHERE s.testId = :id")
    int updateConfigAndData(
            @Param("id") Integer testId,
            @Param("config") String config,
            @Param("data") String data
    );
}
