package com.example.pigfarmmanagementapp.model;

public class User {
    private String username;
    private String email;
    private String password;
    private String role;

    public User(String username, String email, String role){

    }

    public User(String username, String email,  String password,  String role){
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String fullname){
        this.username = fullname;
    }

    public String getEmail(){
        return  email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public  String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

}
