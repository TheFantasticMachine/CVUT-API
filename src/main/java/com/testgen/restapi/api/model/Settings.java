package com.testgen.restapi.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "system_settings")
public class Settings {

    @Id
    @Column(name = "setting_key", nullable = false)
    private String settingsKey;

    @Column(name = "setting_value", nullable = false)
    private String value;

    @Column(name = "description")
    private String description;

    public Settings() {}

    public Settings(String settingsKey, String value, String description) {
        this.settingsKey = settingsKey;
        this.value = value;
        this.description = description;
    }

    public String getSettingsKey() {
        return settingsKey;
    }

    public void setSettingsKey(String settingsKey) {
        this.settingsKey = settingsKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
