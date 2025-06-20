package com.example.pigfarmmanagementapp.model;

public class PigStatusAdvice {
    private String temperature;
    private String humidity;
    private String stressLevel;
    private String advisory;

    public PigStatusAdvice(String temperature, String humidity, String stressLevel, String advisory) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.stressLevel = stressLevel;
        this.advisory = advisory;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getStressLevel() {
        return stressLevel;
    }

    public String getAdvisory() {
        return advisory;
    }
}