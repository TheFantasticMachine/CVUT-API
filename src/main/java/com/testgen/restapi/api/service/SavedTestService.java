package com.testgen.restapi.api.service;

import tools.jackson.databind.ObjectMapper;
import com.testgen.restapi.api.controller.SaveTestApiController;
import com.testgen.restapi.api.model.SavedTest;
import com.testgen.restapi.api.repo.SaveTestRepo;
import com.testgen.restapi.api.repo.SettingsRepo;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SavedTestService {

    private final SaveTestRepo saveTestRepo;
    private final SettingsRepo settingsRepo;
    private final ObjectMapper objectMapper;

    @Autowired
    public SavedTestService(SaveTestRepo saveTestRepo, SettingsRepo settingsRepo, ObjectMapper objectMapper) {
        this.saveTestRepo = saveTestRepo;
        this.settingsRepo = settingsRepo;
        this.objectMapper = objectMapper;
    }

    public SavedTest createNewTest(int userId, String title, String subjectName, int subjectId) {
        int testLimit = Integer.parseInt(settingsRepo.getSettingValueByKey("MAX_TESTS_PER_TEACHER"));
        int userTestCount = saveTestRepo.countByUserId(userId);

        if (userTestCount >= testLimit) {
            throw new IllegalStateException("Test quota reached. Maximum allowed: " + testLimit);
        }

        List<String> variantLetters = new ArrayList<>();
        variantLetters.add("A");
        variantLetters.add("B");
        variantLetters.add("C");

        JSONObject configJson = new JSONObject();
        configJson.put("title", title);
        configJson.put("subject_name", subjectName);
        configJson.put("question_max", 15);
        configJson.put("variant_max", 3);
        configJson.put("variants", new JSONArray(variantLetters));

        JSONObject dataJson = new JSONObject();
        for (String letter : variantLetters ) {
            dataJson.put(letter, new JSONArray());
        }

        SavedTest test = new SavedTest();
        test.setUserId(userId);
        test.setSubjectId(subjectId);
        test.setStatus("DRAFT");
        test.setDueDate(null);
        test.setTestConfig(configJson.toString());
        test.setTestData(dataJson.toString());

        return saveTestRepo.save(test);
    }

    public List<SaveTestApiController.TestSummary> getTestSummariesByUserId (int userId) {
        List<SaveTestApiController.TestSummary> result = new ArrayList<>();
        List<SavedTest> savedTests = saveTestRepo.findAllByUserId(userId);
        System.out.println(savedTests.size());

        for (SavedTest test : savedTests) {
            JSONObject config = new JSONObject(test.getTestConfig());

            SaveTestApiController.TestSummary summary = new SaveTestApiController.TestSummary(
                    test.getTestId(),
                    config.getString("title"),
                    config.getString("subject_name"),
                    test.getStatus(),
                    test.getDueDate(),
                    test.getLastEditAt(),
                    config.getJSONArray("variants").toList()
            );

            result.add(summary);
        }

        return result;
    }

    @Transactional
    public SavedTest updateTest(Integer testId, SaveTestApiController.UpdateRequest request) throws Exception {
        // 1. Fetch the existing entity by ID (Hibernate tracks this managed entity)
        SavedTest existingTest = saveTestRepo.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with ID: " + testId));

        // 2. Convert incoming objects to JSON strings
        String configJson = objectMapper.writeValueAsString(request.config());
        String dataJson = objectMapper.writeValueAsString(request.data());

        // 3. Mutate fields on the tracked entity
        existingTest.setTestConfig(configJson);
        existingTest.setTestData(dataJson);

        // 4. Save triggers SQL UPDATE, keeping testId intact
        return saveTestRepo.save(existingTest);
    }

    public Optional<SavedTest> getTestByID (int testId) {
        Optional<SavedTest> savedTest = saveTestRepo.getSavedTestByTestId(testId);
        if (savedTest.isPresent()) {
            return savedTest;
        }
        return Optional.empty();
    }
}
