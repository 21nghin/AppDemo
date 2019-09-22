package com.t3h.appdemo.model;

public class User {

    private String Name;
    private String Email;
    private String Passwrod;
    private String Image;

    public User() {
    }

    public User(String name, String email, String passwrod) {
        Name = name;
        Email = email;
        Passwrod = passwrod;
    }

    public User(String name, String email, String passwrod, String image) {
        Name = name;
        Email = email;
        Passwrod = passwrod;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPasswrod() {
        return Passwrod;
    }

    public void setPasswrod(String passwrod) {
        Passwrod = passwrod;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
