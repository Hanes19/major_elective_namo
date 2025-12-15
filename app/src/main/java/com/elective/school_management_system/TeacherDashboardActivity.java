package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

    // Bottom Navigation
    private LinearLayout navMaps, navDashboard, navUpdates;

    // Dashboard Buttons
    private LinearLayout btnAssignedRooms, btnMySchedule;
    private LinearLayout btnNavFaculty, btnNavAdmin, btnNavClinic;
    private FrameLayout imgLogo; // Profile button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_dashboard);

        initViews();
        setupListeners();
    }

    private void initViews() {
        // Bottom Nav
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        // Main Action Buttons
        btnAssignedRooms = findViewById(R.id.btnAssignedRooms);
        btnMySchedule = findViewById(R.id.btnMySchedule);

        // Quick Nav Buttons
        btnNavFaculty = findViewById(R.id.btnNavFaculty);
        btnNavAdmin = findViewById(R.id.btnNavAdmin);
        btnNavClinic = findViewById(R.id.btnNavClinic);

        // Profile
        imgLogo = findViewById(R.id.imgLogo);
    }

    private void setupListeners() {
        // --- Bottom Navigation ---
        navMaps.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherRoomsActivity.class));
            overridePendingTransition(0, 0); // Optional: No animation for cleaner nav
        });

        navDashboard.setOnClickListener(v -> {
            // Already here
        });

        navUpdates.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherUpdatesActivity.class));
            overridePendingTransition(0, 0);
        });

        // --- Main Buttons ---
        btnAssignedRooms.setOnClickListener(v ->
                startActivity(new Intent(this, TeacherAssignedRoomsActivity.class)));

        btnMySchedule.setOnClickListener(v ->
                Toast.makeText(this, "Full Schedule View Coming Soon", Toast.LENGTH_SHORT).show());

        // --- Quick Nav & Profile ---
        imgLogo.setOnClickListener(v ->
                Toast.makeText(this, "Profile Settings Clicked", Toast.LENGTH_SHORT).show());

        btnNavFaculty.setOnClickListener(v -> showToast("Faculty Room navigation"));
        btnNavAdmin.setOnClickListener(v -> showToast("Admin Office navigation"));
        btnNavClinic.setOnClickListener(v -> showToast("Clinic navigation"));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Prevent back button from returning to Login immediately
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}