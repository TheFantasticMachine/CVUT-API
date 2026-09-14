package com.testgen.restapi.api.repo;

import com.testgen.restapi.api.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepo extends JpaRepository<Settings, String> {

    @Query("SELECT s.settingsValue FROM Settings s WHERE UPPER(s.settingsKey) = UPPER(:key)")
    String getSettingValueByKey(@Param("key") String key);
}
