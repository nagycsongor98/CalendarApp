package com.nagycsongor.calendarapp;

import java.util.HashMap;

public class User {

    private String email, name, password;
    private HashMap<String,Event> events = new HashMap<>();

    public User() {

    }

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public HashMap<String, Event> getEvents() {
        return events;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
