package com.t3h.appdemo.model;

public class User {

    private String Name;
    private String Email;
    private String Passwrod;

    public User() {
    }

    public User(String name, String email, String passwrod) {
        Name = name;
        Email = email;
        Passwrod = passwrod;
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
}
