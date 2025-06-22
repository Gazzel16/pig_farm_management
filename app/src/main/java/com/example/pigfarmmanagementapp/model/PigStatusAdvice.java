package com.example.pigfarmmanagementapp.model;

public class PigStatusAdvice {
    private int temperature;
    private int humidity;
    private String stressLevel;
    private String advisory;

    public PigStatusAdvice(int temperature, int humidity, String stressLevel, String advisory) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.stressLevel = stressLevel;
        this.advisory = advisory;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getStressLevel() {
        return stressLevel;
    }

    public String getAdvisory() {
        return advisory;
    }
}