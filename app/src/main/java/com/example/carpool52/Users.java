package com.example.carpool52;

public class Users {
    private int uid;
    private String name;
    private String email;
    private int status;
    private int rating;

    public Users() {}

    public Users(int uid, String name, String email, int status, int rating) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.status = status;
        this.rating = rating;
    }

    public String getName() {return name;}
    public String getEmail() {return email;}
    public int getUid() {return uid;}
    public int getStatus() {return status;}
    public int getRating() {return rating;}

    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setUid(int uid) {this.uid = uid;}
    public void setStatus(int status) {this.status = status;}
    public void setRating(int rating) {this.rating = rating;}
}
