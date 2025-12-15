package com.elective.school_management_system;

public class Event {
    private int id;
    private String title;
    private String description;
    private String date; // Format "YYYY-MM-DD"
    private String type; // "General" or "Emergency"

    public Event(int id, String title, String description, String date, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getType() { return type; }
}