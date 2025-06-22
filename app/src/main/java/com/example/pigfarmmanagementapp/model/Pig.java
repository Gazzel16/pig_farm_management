package com.example.pigfarmmanagementapp.model;

public class Pig {
    private String id;
    private String breed; // New field
    private String birthDate; // New field
    private double weight; // New field
    private String vaccinationStatus; // New field

    // Default constructor
    public Pig() {
    }

    // Constructor with all fields (excluding name)
    public Pig(String id, String breed,String birthDate, double weight, String vaccinationStatus) {
        this.id = id;
        this.breed = breed;
        this.birthDate = birthDate;
        this.weight = weight;
        this.vaccinationStatus = vaccinationStatus;
    }

    // Getter and Setter for ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter for Breed
    public String getBreed() {
        return breed != null ? breed : ""; // Ensure non-null
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    // Getter and Setter for Weight
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    // Getter and Setter for Vaccination Status

    public void setVaccinationStatus(String vaccinationStatus) {
        this.vaccinationStatus = vaccinationStatus;
    }

    public String getVaccinationStatus() {
        return vaccinationStatus;
    }

    public String vaccinationStatus() {
        return vaccinationStatus;
    }

}
