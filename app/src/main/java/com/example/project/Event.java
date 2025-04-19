package com.example.project.ui.home;

public class Event {
    private final String title;
    private final String date;        // e.g. "April 19, 2025"
    private final String description;

    public Event(String title, String date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getTitle()       { return title; }
    public String getDate()        { return date; }
    public String getDescription() { return description; }
}
