package com.example.pigfarmmanagementapp.model;

public class Pig {
    private String id;
    private String breed; // New field

    private String gender;
    private String birthDate; // New field
    private double weight; // New field
    private String vaccinationStatus; // New field

    private String pigIllness;

    private String lastCheckUp;

    private String cageId;

    private boolean purchase = false;

    // Default constructor
    public Pig() {
    }

    // Constructor with all fields (excluding name)
    public Pig(String id, String breed,String gender, String birthDate,
               double weight, String pigIllness, String vaccinationStatus,
               String lastCheckUp, String cageId,  boolean purchase) {
        this.id = id;
        this.breed = breed;
        this.gender = gender;
        this.birthDate = birthDate;
        this.weight = weight;
        this.pigIllness = pigIllness;
        this.vaccinationStatus = vaccinationStatus;
        this.lastCheckUp = lastCheckUp;
        this.cageId = cageId;
        this.purchase = purchase;

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

    //Pig Illness
    public void setPigIllness(String pigIllness) {
        this.pigIllness = pigIllness;
    }
    public String getPigIllness(){
        return pigIllness;
    }
    public  String pigIllness(){
        return pigIllness;
    }

    //Pig Gender

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public String gender(){
        return gender;
    }

    //Last CheckUp

    public void setLastCheckUp(String lastCheckUp) {
        this.lastCheckUp = lastCheckUp;
    }

    public String getLastCheckUp() {
        return lastCheckUp;
    }

    public String lastCheckUp(){
        return lastCheckUp;
    }

    //Cage
    public String getCageId() {
        return cageId;
    }

    // Optional: Setter
    public void setCageId(String cageId) {
        this.cageId = cageId;
    }

    public boolean isPurchase() {
        return purchase;
    }

    public void setPurchase(boolean purchase) {
        this.purchase = purchase;
    }
}
