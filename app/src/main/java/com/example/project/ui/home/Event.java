package com.example.project.model;

public class Event {

    private String eventName;
    private String eventDate;
    private String eventDescription;
    private int eventPosterResource; // Image resource ID

    // Constructor
    public Event(String eventName, String eventDate, String eventDescription, int eventPosterResource) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.eventPosterResource = eventPosterResource;
    }

    // Getters for the event details
    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public int getEventPosterResource() {
        return eventPosterResource;
    }
}
