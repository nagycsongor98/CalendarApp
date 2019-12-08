package com.nagycsongor.calendarapp;

import java.util.ArrayList;
import java.util.Date;

public class Event {
    private String title;
    private Date eventDate;
    private Date reminderDate;
    private ArrayList<String> invitedFriends;
    private String location;
    private String description;

    public Event(){
        // Need to use public empty ctor for firebase.
    }

    public Event(String title, Date eventDate, Date reminderDate, String location, String description) {
        this.title = title;
        this.eventDate = eventDate;
        this.reminderDate = reminderDate;
        this.location = location;
        this.description = description;
        this.invitedFriends = new ArrayList<String>();
    }

    public void addFriendByEmail(String friendEmail){
        invitedFriends.add(friendEmail);
    }

    public String getTitle() {
        return title;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public ArrayList<String> getInvitedFriends() {
        return invitedFriends;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}
