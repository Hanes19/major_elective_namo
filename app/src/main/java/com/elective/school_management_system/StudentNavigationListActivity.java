package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentNavigationListActivity extends AppCompatActivity {

    // UI Components matching s_navigation_list.xml
    private LinearLayout cardRooms, cardInstructors;
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
        // Main Cards
        cardRooms = findViewById(R.id.card_rooms);
        cardInstructors = findViewById(R.id.card_instructors);

        // Settings button (FrameLayout in XML)
        btnSettings = findViewById(R.id.img_settings);

        // Bottom Navigation
        navHome = findViewById(R.id.navHome);
        navNav = findViewById(R.id.navNav);
        navProfile = findViewById(R.id.navProfile);
    }

    private void setupListeners() {
        // --- Main Action Cards ---

        // 1. All Rooms (was btnManageMap)
        cardRooms.setOnClickListener(v -> {
            Intent intent = new Intent(StudentNavigationListActivity.this, StudentRoomsListActivity.class);
            startActivity(intent);
        });

        // 2. Instructors (was btnManageUsers)
        cardInstructors.setOnClickListener(v -> {
            Intent intent = new Intent(StudentNavigationListActivity.this, StudentInstructorsListActivity.class);
            startActivity(intent);
        });

        // Settings Button
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            });
        }

        // --- Bottom Navigation ---

        // Home / Dashboard
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(StudentNavigationListActivity.this, StudentDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // Optional: finish this activity to remove it from stack
        });

        // Navigation (Current Screen)
        navNav.setOnClickListener(v -> {
            // Already on Navigation List
        });

        // Profile
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(StudentNavigationListActivity.this, StudentProfileActivity.class);
            startActivity(intent);
            finish(); // Optional
        });
    }
}