package com.elective.school_management_system;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SampleDataDebuggerActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView logView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // 2. Create the UI Programmatically
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        scrollView.addView(layout);

        // Title
        TextView title = new TextView(this);
        title.setText("Sample Data Generator");
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);

        // Buttons
        layout.addView(createButton("Add Sample Users (Student/Teacher)", v -> addSampleUsers()));
        layout.addView(createButton("Add Sample Rooms", v -> addSampleRooms()));
        layout.addView(createButton("Add Sample Instructors", v -> addSampleInstructors()));
        layout.addView(createButton("Add Sample Reports", v -> addSampleReports()));
        layout.addView(createButton("Add Sample Schedule & Events", v -> addSampleScheduleAndEvents()));

        // Spacer
        View spacer = new View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50));
        layout.addView(spacer);

        // Destructive Actions
        Button btnReset = createButton("⚠️ RESET DATABASE (Clear All)", v -> resetDatabase());
        btnReset.setTextColor(Color.RED);
        layout.addView(btnReset);

        // Log Output
        logView = new TextView(this);
        logView.setText("Logs will appear here...");
        logView.setPadding(0, 30, 0, 0);
        layout.addView(logView);

        setContentView(scrollView);
    }

    // --- Helper to Create Buttons ---
    private Button createButton(String text, View.OnClickListener listener) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setOnClickListener(listener);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 10, 0, 10);
        btn.setLayoutParams(params);
        return btn;
    }

    private void log(String message) {
        logView.setText(message + "\n" + logView.getText());
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // ================= DATA GENERATION FUNCTIONS =================

    private void addSampleUsers() {
        if (dbHelper.registerUser("Test Student", "student_test@school.com", "123456")) {
            log("Added: Test Student");
            // Add a profile for them automatically
            int id = dbHelper.getUserId("student_test@school.com");
            dbHelper.saveUserProfile(id, "BSIT", "4th Year", "Section A", "09123456789");
        } else {
            log("Error: Student email might already exist.");
        }

        if (dbHelper.registerUser("Test Teacher", "teacher_test@school.com", "123456")) {
            // Manually update role to Teacher since registerUser defaults to Student
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("role", "Teacher");
            db.update("users", values, "email=?", new String[]{"teacher_test@school.com"});
            log("Added: Test Teacher");
        }
    }

    private void addSampleRooms() {
        if (dbHelper.addRoom("Lab 505", "Advanced Robotics Lab")) {
            log("Added: Lab 505");
        }
        if (dbHelper.addRoom("Lecture Hall B", "Audio Visual Room")) {
            log("Added: Lecture Hall B");
        }
    }

    private void addSampleInstructors() {
        if (dbHelper.addInstructor("Dr. John Doe", "Computer Science")) {
            log("Added: Dr. John Doe");
        }
        if (dbHelper.addInstructor("Ms. Jane Smith", "Information Technology")) {
            log("Added: Ms. Jane Smith");
        }
    }

    private void addSampleReports() {
        if (dbHelper.addReport("Lab 505", "Monitor not working at station 3", "Hardware")) {
            log("Added Report: Hardware issue in Lab 505");
        }
    }

    private void addSampleScheduleAndEvents() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Since there is no generic public addSchedule method in your Helper, we insert directly
        try {
            // Get IDs (assuming data exists, otherwise fallback to 1)
            int teacherId = dbHelper.getUserId("teacher_test@school.com");
            if (teacherId == -1) teacherId = 2; // Fallback to initial data teacher

            ContentValues sch = new ContentValues();
            sch.put("user_id", teacherId);
            sch.put("subject", "Robotics 101");
            sch.put("room_name", "Lab 505");
            sch.put("day_of_week", "Monday");
            sch.put("start_time", "13:00");
            sch.put("end_time", "15:00");
            db.insert("schedules", null, sch);

            ContentValues evt = new ContentValues();
            evt.put("title", "Hackathon 2025");
            evt.put("description", "Annual coding competition");
            evt.put("date", "2025-11-15");
            evt.put("type", "General");
            db.insert("events", null, evt);

            log("Added: Schedule for Teacher ID " + teacherId + " and Hackathon Event");
        } catch (Exception e) {
            log("Error adding schedule: " + e.getMessage());
        }
    }

    private void resetDatabase() {
        try {
            // This deletes the physical database file
            this.deleteDatabase("SchoolSystem.db");
            // Re-initialize to recreate tables via onCreate
            dbHelper = new DatabaseHelper(this);
            dbHelper.getWritableDatabase(); // Triggers onCreate
            log("DATABASE RESET: All data cleared and defaults restored.");
        } catch (Exception e) {
            log("Reset failed: " + e.getMessage());
        }
    }
}