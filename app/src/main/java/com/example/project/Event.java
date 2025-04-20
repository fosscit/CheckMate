package com.example.project;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    // Method to check if the event date is today
    public boolean isToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date()); // Get current date in "yyyy-MM-dd" format
        return eventDate.equals(currentDate); // Return true if the event date is today
    }
}
