// File: app/src/main/java/com/example/project/ui/home/Event.java
package com.example.project.ui.home;

public class Event {
    private final String title;
    private final String date;
    private final String description;
    private final int imageResId;

    public Event(String title,
                 String date,
                 String description,
                 int imageResId) {
        this.title       = title;
        this.date        = date;
        this.description = description;
        this.imageResId  = imageResId;
    }

    public String getTitle()       { return title; }
    public String getDate()        { return date; }
    public String getDescription() { return description; }
    public int    getImageResId()  { return imageResId; }
}
