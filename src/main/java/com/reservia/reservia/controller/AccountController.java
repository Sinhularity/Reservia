package com.reservia.reservia.controller;

public class AccountController {

    private String  email;

    private String  maternalSurname;
    private String  paternalSurname;
    private String username;

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return paternalSurname + " " + maternalSurname + " " + username;
    }
}
