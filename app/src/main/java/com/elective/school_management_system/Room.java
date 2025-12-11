package com.elective.school_management_system;

public class Room {
    private int id;
    private String roomName;
    private String description;
    private String arDestinationId; // The ID Unity needs to navigate (e.g., "room_101")

    public Room(int id, String roomName, String description, String arDestinationId) {
        this.id = id;
        this.roomName = roomName;
        this.description = description;
        this.arDestinationId = arDestinationId;
    }

    public int getId() { return id; }
    public String getRoomName() { return roomName; }
    public String getDescription() { return description; }
    public String getArDestinationId() { return arDestinationId; }
}