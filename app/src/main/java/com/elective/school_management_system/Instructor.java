package com.elective.school_management_system;

public class Instructor {
    private int id;
    private String name;
    private String department;

    public Instructor(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
}