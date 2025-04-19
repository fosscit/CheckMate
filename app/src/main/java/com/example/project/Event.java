package com.example.project;

public class Event {
    private final String eventName;
    private final String eventDate;
    private final String eventDescription;
    private final String posterPath;

    public Event(String eventName, String eventDate, String eventDescription, String posterPath) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.posterPath = posterPath;
    }

    public String getEventName() { return eventName; }
    public String getEventDate() { return eventDate; }
    public String getEventDescription() { return eventDescription; }
    public String getPosterPath() { return posterPath; }
}