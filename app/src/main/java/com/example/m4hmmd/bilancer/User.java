package com.example.m4hmmd.bilancer;

public class User {

    // Properties
    //private Uri myAvatar;
    private String name;
    private String surname;
    private String email;
    private String username;
   // private double rating;
    //private String description;
    //private String contactInfo;

    // Constructors
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User( String name,String surname,String username, String email) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() { return username;  }
}