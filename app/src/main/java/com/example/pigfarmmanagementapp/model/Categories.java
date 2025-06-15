package com.example.pigfarmmanagementapp.model;

public class Categories {

    private String description;
    private String title;
    private int imageRes;

    public Categories (int imageRes,  String title, String description){

        this.imageRes = imageRes;
        this.title = title;
        this.description = description;
    }


    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public int getimageRes(){
        return imageRes;
    }

}
