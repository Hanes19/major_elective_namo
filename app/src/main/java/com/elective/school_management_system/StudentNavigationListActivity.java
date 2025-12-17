package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentNavigationListActivity extends AppCompatActivity {

    // UI Components
    private LinearLayout cardRooms, cardInstructors, cardSchedule;
    private View mapBanner; // <--- [1] ADD THIS VARIABLE
    private LinearLayout navHome, navNav, navProfile;
    private View btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_navigation_list);

        initViews();
        setupListeners();
    }

    private void initViews() {
        // Main Action Cards
        cardRooms = findViewById(R.id.card_rooms);
        cardInstructors = findViewById(R.id.card_instructors);
        cardSchedule = findViewById(R.id.card_schedule);

        // [2] FIND THE MAP BANNER VIEW
        mapBanner = findViewById(R.id.map_banner);

        // Settings button
        btnSettings = findViewById(R.id.img_settings);

        // Bottom Navigation
        navHome = findViewById(R.id.navHome);
        navNav = findViewById(R.id.navNav);
        navProfile = findViewById(R.id.navProfile);
    }

    private void setupListeners() {
        // --- Main Action Cards ---

        // 1. All Rooms
        cardRooms.setOnClickListener(v -> {
            Intent intent = new Intent(StudentNavigationListActivity.this, StudentRoomsListActivity.class);
            startActivity(intent);
        });

        // 2. Instructors
        cardInstructors.setOnClickListener(v -> {
            Intent intent = new Intent(StudentNavigationListActivity.this, StudentInstructorsListActivity.class);
            startActivity(intent);
        });

        // 3. My Schedule
        cardSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(StudentNavigationListActivity.this, StudentScheduleActivity.class);
            startActivity(intent);
        });

        // [3] ADD THE CLICK LISTENER FOR THE MAP BANNER
        if (mapBanner != null) {
            mapBanner.setOnClickListener(v -> {
                // Assuming you want to go to the Map Activity
                Intent intent = new Intent(StudentNavigationListActivity.this, StudentRoomMapActivity.class);
                startActivity(intent);
            });
        }

        // Settings Button
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                // Example: Intent to Settings
                Intent intent = new Intent(StudentNavigationListActivity.this, NavSettingsActivity.class);
                startActivity(intent);
            });
        }

        // --- Bottom Navigation ---

        // Home / Dashboard
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(StudentNavigationListActivity.this, StudentDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Navigation (Current Screen)
        navNav.setOnClickListener(v -> {
            // Already here, do nothing
        });

        // Profile
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(StudentNavigationListActivity.this, StudentProfileActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}