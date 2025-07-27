package com.example.pigfarmmanagementapp.model;

public class AnalyticsCategories {
    private String title;
    private String description;
    private int iconResId;
    private int progress;

    public AnalyticsCategories(String title, String description, int iconResId, int progress) {
        this.title = title;
        this.description = description;
        this.iconResId = iconResId;
        this.progress = progress;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getIconResId() { return iconResId; }
    public int getProgress() { return progress; }
}
