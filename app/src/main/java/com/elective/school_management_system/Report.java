package com.elective.school_management_system;

public class Report {
    private int id;
    private String roomName;
    private String description;
    private String category;
    private String status;
    private String date;

    public Report(int id, String roomName, String description, String category, String status, String date) {
        this.id = id;
        this.roomName = roomName;
        this.description = description;
        this.category = category;
        this.status = status;
        this.date = date;
    }

    public int getId() { return id; }
    public String getRoomName() { return roomName; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public String getDate() { return date; }
}