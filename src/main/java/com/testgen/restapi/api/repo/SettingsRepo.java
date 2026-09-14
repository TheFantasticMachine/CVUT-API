package com.testgen.restapi.api.repo;

import com.testgen.restapi.api.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepo extends JpaRepository<Settings, String> {

    String getBySettingsKeyIgnoreCase(String settingsKey);
}
