
package com.example.project.ui.home;

public class Event {
    private String title;
    private String date;
    private String description;

    public Event(String title, String date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
}
