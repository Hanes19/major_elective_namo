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
    private View mapBanner;

    // Suggestions Items
    private LinearLayout item1, item2, item3;

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

        // Map Banner
        mapBanner = findViewById(R.id.map_banner);

        // [1] Initialize Suggestion Items
        item1 = findViewById(R.id.item_1); // Next Class
        item2 = findViewById(R.id.item_2); // Library
        item3 = findViewById(R.id.item_3); // Last Search

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

        // Map Banner -> Regular Map
        if (mapBanner != null) {
            mapBanner.setOnClickListener(v -> {
                Intent intent = new Intent(StudentNavigationListActivity.this, StudentRoomMapActivity.class);
                startActivity(intent);
            });
        }

        // --- [2] SUGGESTIONS LISTENERS (Linked to AR Mode) ---

        // Suggestion 1: Next Class (Computer Lab 3)
        if (item1 != null) {
            item1.setOnClickListener(v -> {
                Intent intent = new Intent(StudentNavigationListActivity.this, StudentNavigationActivity.class);
                intent.putExtra("ROOM_NAME", "Computer Lab 3");
                startActivity(intent);
            });
        }

        // Suggestion 2: Library
        if (item2 != null) {
            item2.setOnClickListener(v -> {
                Intent intent = new Intent(StudentNavigationListActivity.this, StudentNavigationActivity.class);
                intent.putExtra("ROOM_NAME", "Library");
                startActivity(intent);
            });
        }

        // Suggestion 3: Last Search (Room 103)
        if (item3 != null) {
            item3.setOnClickListener(v -> {
                Intent intent = new Intent(StudentNavigationListActivity.this, StudentNavigationActivity.class);
                intent.putExtra("ROOM_NAME", "Room 103");
                startActivity(intent);
            });
        }

        // Settings Button
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(StudentNavigationListActivity.this, NavSettingsActivity.class);
                startActivity(intent);
            });
        }

        // --- Bottom Navigation ---

        // Home / Dashboard
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(StudentNavigationListActivity.this, StudentDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            });
        }

        // Navigation (Current Screen)
        if (navNav != null) {
            navNav.setOnClickListener(v -> {
                // Already here, do nothing
            });
        }

        // Profile
        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                Intent intent = new Intent(StudentNavigationListActivity.this, StudentProfileActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }
    }
}