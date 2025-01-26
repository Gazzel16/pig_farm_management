package com.example.pigfarmmanagementapp.model;

public class Cage {
    private String name;
    private String status;
    private String id;

    // No-argument constructor required by Firebase
    public Cage() {
        // Firebase uses this constructor to create an empty instance of the class
    }

    public Cage (String name, String status, String id){
        this.name = name;
        this.status = status;
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public String getStatus(){
        return status;
    }

    public String getId(){
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}
