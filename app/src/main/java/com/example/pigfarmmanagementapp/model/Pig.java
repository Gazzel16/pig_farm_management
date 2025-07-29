package com.example.pigfarmmanagementapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Pig {
    private String id;
    private String breed; // New field

    private String gender;
    private String birthDate; // New field
    private double weight; // New field
    private String vaccinationStatus; // New field

    private String pigIllness;

    private String lastCheckUp;
    private String nextCheckUp;

    private String cageId;

    private boolean purchase = false;

    private String buyerName;
    private String buyerContact;

    private String purchaseDateTime;


    // Default constructor
    public Pig() {
    }

    // Constructor with all fields (excluding name)
    public Pig(String id, String breed,String gender, String birthDate,
               double weight, String pigIllness, String vaccinationStatus,
               String lastCheckUp, String cageId,  boolean purchase,
               String buyerName,
               String buyerContact, String purchaseDateTime, String nextCheckUp) {
        this.id = id;
        this.breed = breed;
        this.gender = gender;
        this.birthDate = birthDate;
        this.weight = weight;
        this.pigIllness = pigIllness;
        this.vaccinationStatus = vaccinationStatus;

        this.lastCheckUp = lastCheckUp;
        this.nextCheckUp = nextCheckUp;

        this.cageId = cageId;
        this.purchase = purchase;

        this.buyerName = buyerName;
        this.buyerContact = buyerContact;
        this.purchaseDateTime = purchaseDateTime;

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

    public void setNextCheckUp(String nextCheckUp){
        this.nextCheckUp = nextCheckUp;
    }

    public String getNextCheckUp(){
        return nextCheckUp;
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

    //Purchase Field
    public boolean isPurchase() {
        return purchase;
    }

    public void setPurchase(boolean purchase) {
        this.purchase = purchase;
    }

    //Buyer name Field
    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    //Buyer Field
    public String getBuyerContact() {
        return buyerContact;
    }

    public void setBuyerContact(String buyerContact) {
        this.buyerContact = buyerContact;
    }

    public String getPurchaseDateTime() {
        return purchaseDateTime;
    }

    public void setPurchaseDateTime(String buyerContact) {
        this.purchaseDateTime = purchaseDateTime;
    }

    public String getCheckupStatus() {
        if (nextCheckUp == null || nextCheckUp.isEmpty()) {
            return "Unknown";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date today = new Date();
            Date next = sdf.parse(nextCheckUp);
            return today.after(next) ? "Overdue" : "On Schedule";
        } catch (Exception e) {
            return "Invalid Date";
        }
    }


}
