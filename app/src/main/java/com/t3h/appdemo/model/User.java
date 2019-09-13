package com.t3h.appdemo.model;

public class User {

    public String FullName;
    public int Age;
    public String Address;
    public String Email;
    public String ProgrammingLanguage;
    public int Experient;

    public User(String fullName, int age, String address, String email, String programmingLanguage, int experient) {
        FullName = fullName;
        Age = age;
        Address = address;
        Email = email;
        ProgrammingLanguage = programmingLanguage;
        Experient = experient;
    }
}
