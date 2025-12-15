package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class StudentDashboardActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;

    // UI Components
    private FrameLayout imgSettings;
    private LinearLayout searchContainer;
    private LinearLayout cardRooms, cardInstructors, cardNav, cardProfile;
    private LinearLayout item1, item2, item3;
    private LinearLayout navHome, navNav, navProfile;

    // DB Helper for suggestions
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_dashboard);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        imgSettings = findViewById(R.id.img_settings);
        searchContainer = findViewById(R.id.search_container);

        // Cards
        cardRooms = findViewById(R.id.card_rooms);
        cardInstructors = findViewById(R.id.card_instructors);
        cardNav = findViewById(R.id.card_nav);
        cardProfile = findViewById(R.id.card_profile);

        // Suggestions (Items)
        item1 = findViewById(R.id.item_1);
        item2 = findViewById(R.id.item_2);
        item3 = findViewById(R.id.item_3);

        // Bottom Nav
        navHome = findViewById(R.id.navHome);
        navNav = findViewById(R.id.navNav);
        navProfile = findViewById(R.id.navProfile);
    }

    private void setupListeners() {
        // --- Header Actions ---
        imgSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Settings feature coming soon", Toast.LENGTH_SHORT).show();
        });

        searchContainer.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, StudentRoomsListActivity.class);
            startActivity(intent);
        });

        // --- Main Grid Cards ---
        cardRooms.setOnClickListener(v -> startActivity(new Intent(this, StudentRoomsListActivity.class)));
        cardInstructors.setOnClickListener(v -> startActivity(new Intent(this, StudentInstructorsListActivity.class)));
        cardNav.setOnClickListener(v -> checkCameraPermissionAndOpen());
        cardProfile.setOnClickListener(v -> startActivity(new Intent(this, StudentProfileActivity.class)));

        // --- Bottom Navigation ---
        navHome.setOnClickListener(v -> {}); // Already on home
        navNav.setOnClickListener(v -> checkCameraPermissionAndOpen());
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, StudentProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // --- SUGGESTIONS LOGIC (NEW) ---

        // Item 1: "Room Computer Lab 3"
        // Search for "Computer" (matches description "Computer Laboratory 1")
        item1.setOnClickListener(v -> navigateToRoom("Computer"));

        // Item 2: "Professor Marcelo Dupalco"
        // Navigate to instructor list and filter for "Marcelo"
        item2.setOnClickListener(v -> navigateToInstructor("Marcelo"));

        // Item 3: "Room 103"
        // Search directly for "Room 103"
        item3.setOnClickListener(v -> navigateToRoom("Room 103"));
    }

    // Helper: Finds a room and opens details
    private void navigateToRoom(String query) {
        List<Room> rooms = dbHelper.searchRooms(query);
        if (!rooms.isEmpty()) {
            // Found a match, open RoomDetails with ID
            Room r = rooms.get(0);
            Intent intent = new Intent(this, RoomDetailsActivity.class);
            intent.putExtra("ROOM_ID", r.getId());
            startActivity(intent);
        } else {
            // Not found in DB, just open the list so user can search manually
            Toast.makeText(this, "Opening Rooms List...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, StudentRoomsListActivity.class);
            startActivity(intent);
        }
    }

    // Helper: Opens instructor list pre-filtered
    private void navigateToInstructor(String query) {
        Intent intent = new Intent(this, StudentInstructorsListActivity.class);
        intent.putExtra("SEARCH_QUERY", query);
        startActivity(intent);
    }

    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(StudentDashboardActivity.this, StudentNavigationActivity.class);
        intent.putExtra("ROOM_NAME", "Navigation Mode");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to use Navigation", Toast.LENGTH_SHORT).show();
            }
        }
    }
}