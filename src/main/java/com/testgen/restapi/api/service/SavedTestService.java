package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.SavedTest;
import com.testgen.restapi.api.repo.SaveTestRepo;
import com.testgen.restapi.api.repo.SettingsRepo;
import com.testgen.restapi.api.repo.SubjectRepo;
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

    @Autowired
    public SavedTestService(SaveTestRepo saveTestRepo, SettingsRepo settingsRepo ) {
        this.saveTestRepo = saveTestRepo;
        this.settingsRepo = settingsRepo;
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

    public List<?> getTestSummariesByUserId (int userId) {
        List<JSONObject> result = new ArrayList<>();
        List<SavedTest> savedTests = saveTestRepo.findAllByUserId(userId);

        for (SavedTest test : savedTests) {
            JSONObject config = new JSONObject(test.getTestConfig());
            JSONObject summary = new JSONObject();
            summary.put("testId", test.getTestId());
            summary.put("title", config.getString("title"));
            summary.put("subjectName", config.getString("subject_name"));
            summary.put("status", test.getStatus());
            summary.put("dueDate", test.getDueDate());
            summary.put("lastEditAt", test.getLastEditAt());
            summary.put("variants", config.getJSONArray("variants"));
            result.add(summary);
        }

        return result;
    }

    public Optional<SavedTest> getTestByID (int testId) {
        Optional<SavedTest> savedTest = saveTestRepo.getSavedTestByTestId(testId);
        if (savedTest.isPresent()) {
            return savedTest;
        }
        return Optional.empty();
    }
}
