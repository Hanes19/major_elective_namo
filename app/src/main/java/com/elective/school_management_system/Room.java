package com.elective.school_management_system;

public class Room {
    private int id;
    private String roomName;
    private String description;
    private String arDestinationId;
    private double latitude;
    private double longitude;

    // Original Constructor (Kept for DatabaseHelper compatibility)
    public Room(int id, String roomName, String description, String arDestinationId) {
        this(id, roomName, description, arDestinationId, 0.0, 0.0);
    }

    // New Constructor (For Map Activity)
    public Room(int id, String roomName, String description, String arDestinationId, double latitude, double longitude) {
        this.id = id;
        this.roomName = roomName;
        this.description = description;
        this.arDestinationId = arDestinationId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() { return id; }
    public String getRoomName() { return roomName; }
    public String getDescription() { return description; }
    public String getArDestinationId() { return arDestinationId; }

    // Added Getters
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}