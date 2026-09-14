package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.SavedTest;
import com.testgen.restapi.api.repo.SaveTestRepo;
import com.testgen.restapi.api.repo.SettingsRepo;
import com.testgen.restapi.api.repo.SubjectRepo;
import org.hibernate.mapping.Collection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SavedTestService {

    private final SaveTestRepo saveTestRepo;
    private final SettingsRepo settingsRepo;
    private final SubjectRepo subjectRepo;

    @Autowired
    public SavedTestService(SaveTestRepo saveTestRepo, SettingsRepo settingsRepo, SubjectRepo subjectRepo) {
        this.saveTestRepo = saveTestRepo;
        this.settingsRepo = settingsRepo;
        this.subjectRepo = subjectRepo;
    }

    public SavedTest createNewTest(int userId, String title, String subjectName) {
        int testLimit = Integer.parseInt(settingsRepo.getBySettingsKeyIgnoreCase("MAX_TESTS_PER_TEACHER"));
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
        test.setSubjectId(subjectRepo.findBySubjectName(subjectName).getSubjectID());
        test.setStatus("DRAFT");
        test.setDueDate(null);
        test.setTestConfig(configJson.toString());
        test.setTestData(dataJson.toString());

        return saveTestRepo.save(test);
    }
}
