package com.example.pigfarmmanagementapp.model;

public class PigSalesStats {
    public int sold = 0;
    public int maleSold = 0;
    public int femaleSold = 0;

    // Optional: Constructor
    public PigSalesStats() {
    }

    public PigSalesStats(int sold, int maleSold, int femaleSold) {
        this.sold = sold;
        this.maleSold = maleSold;
        this.femaleSold = femaleSold;
    }

    // Optional: Getters and Setters (if needed for future use)
    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getMaleSold() {
        return maleSold;
    }

    public void setMaleSold(int maleSold) {
        this.maleSold = maleSold;
    }

    public int getFemaleSold() {
        return femaleSold;
    }

    public void setFemaleSold(int femaleSold) {
        this.femaleSold = femaleSold;
    }
}
