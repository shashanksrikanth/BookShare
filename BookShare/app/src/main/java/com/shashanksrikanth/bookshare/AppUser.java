package com.shashanksrikanth.bookshare;

public class AppUser {
    // A class that represents a user of the app

    public String firstName;
    public String lastName;
    public String email;

    public AppUser() {}

    public AppUser(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
