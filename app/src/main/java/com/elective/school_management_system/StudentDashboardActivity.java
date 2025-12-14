package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.FrameLayout; // Import added
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StudentDashboardActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;

    // UI Components
    private FrameLayout imgSettings; // CHANGED: from ImageView to FrameLayout to match XML
    private LinearLayout searchContainer;
    private LinearLayout cardRooms, cardInstructors, cardNav, cardProfile;
    private RelativeLayout item1, item2, item3;

    // Bottom Navigation
    private LinearLayout navHome, navNav, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_dashboard);

        initViews();
        setupListeners();
    }

    private void initViews() {
        // This now correctly casts the FrameLayout from your XML
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
        cardRooms.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, StudentRoomsListActivity.class);
            startActivity(intent);
        });

        cardInstructors.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, StudentInstructorsListActivity.class);
            startActivity(intent);
        });

        // Navigation Card -> Check Perms -> Open Camera
        cardNav.setOnClickListener(v -> checkCameraPermissionAndOpen());

        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, StudentProfileActivity.class);
            startActivity(intent);
        });

        // --- Bottom Navigation ---
        // Home (already here)
        navHome.setOnClickListener(v -> {});

        // Nav Button -> Check Perms -> Open Camera
        navNav.setOnClickListener(v -> checkCameraPermissionAndOpen());

        // Profile Button
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, StudentProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // --- Suggestion Items (Optional Logic) ---
        item1.setOnClickListener(v -> Toast.makeText(this, "Suggestion 1 Clicked", Toast.LENGTH_SHORT).show());
        item2.setOnClickListener(v -> Toast.makeText(this, "Suggestion 2 Clicked", Toast.LENGTH_SHORT).show());
        item3.setOnClickListener(v -> Toast.makeText(this, "Suggestion 3 Clicked", Toast.LENGTH_SHORT).show());
    }

    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        // Launches the "AR" screen (which is now just a camera preview)
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