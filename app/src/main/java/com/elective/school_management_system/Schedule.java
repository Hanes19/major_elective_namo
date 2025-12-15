package com.elective.school_management_system;

public class Schedule {
    private int id;
    private int userId;
    private String subject;
    private String roomName;
    private String day;
    private String startTime; // Format: "HH:mm" (24-hour)
    private String endTime;

    public Schedule(int id, int userId, String subject, String roomName, String day, String startTime, String endTime) {
        this.id = id;
        this.userId = userId;
        this.subject = subject;
        this.roomName = roomName;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getSubject() { return subject; }
    public String getRoomName() { return roomName; }
    public String getDay() { return day; } // Added this missing method
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
}