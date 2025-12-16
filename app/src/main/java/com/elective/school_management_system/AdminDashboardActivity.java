package com.elective.school_management_system;

import android.app.AlertDialog; // Added import
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AdminDashboardActivity extends AppCompatActivity {

    // Bottom Navigation Views
    private LinearLayout navMaps, navDashboard, navUpdates;

    // Management Console Buttons
    private ConstraintLayout btnManageMap, btnManageUsers, btnReports;

    // System Overview Cards (Added)
    private LinearLayout cardActiveUsers, cardNewReports, cardSysHealth;

    // Settings Button
    private FrameLayout btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_dashboard);

        initViews();
        setupListeners();
    }

    private void initViews() {
        // Bottom Navigation
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        // Management Console
        btnManageMap = findViewById(R.id.btnManageMap);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnReports = findViewById(R.id.btnReports);

        // System Overview Cards (Initialize)
        cardActiveUsers = findViewById(R.id.cardActiveUsers);
        cardNewReports = findViewById(R.id.cardNewReports);
        cardSysHealth = findViewById(R.id.cardSysHealth);

        // Settings
        btnSettings = findViewById(R.id.btnSettings);
    }

    private void setupListeners() {
        // --- System Overview Listeners (Added) ---

        cardActiveUsers.setOnClickListener(v -> showDescriptionDialog("Active Users",
                "This metric shows the total number of students, teachers, and guests currently active in the system."));

        cardNewReports.setOnClickListener(v -> showDescriptionDialog("New Reports",
                "This indicates the number of pending maintenance or issue reports that require your attention."));

        cardSysHealth.setOnClickListener(v -> showDescriptionDialog("System Health",
                "This percentage represents the current operational stability of the server and database connections."));


        // --- Management Console Listeners ---

        btnManageMap.setOnClickListener(v -> {
            // Navigate to Map Management
            Intent intent = new Intent(AdminDashboardActivity.this, AdminManageRoomsActivity.class);
            startActivity(intent);
        });

        btnManageUsers.setOnClickListener(v -> {
            // Navigate to User Directory
            Intent intent = new Intent(AdminDashboardActivity.this, AdminUserListActivity.class);
            startActivity(intent);
        });

        btnReports.setOnClickListener(v -> {
            // Navigate to Reports
            Intent intent = new Intent(AdminDashboardActivity.this, AdminReportsActivity.class);
            startActivity(intent);
        });

        // --- Settings Listener ---
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, NavSettingsActivity.class);
            startActivity(intent);
        });

        // --- Bottom Navigation Listeners ---

        navMaps.setOnClickListener(v -> {
            // Navigate to Map/Rooms View
            Intent intent = new Intent(AdminDashboardActivity.this, AdminRoomMapActivity.class);
            startActivity(intent);
        });

        navDashboard.setOnClickListener(v -> {
            // Already on Dashboard
            Toast.makeText(this, "You are already on the Dashboard", Toast.LENGTH_SHORT).show();
        });

        navUpdates.setOnClickListener(v -> {
            // Navigate to Updates/Reports View
            Intent intent = new Intent(AdminDashboardActivity.this, AdminReportsActivity.class);
            startActivity(intent);
        });
    }

    // Helper method to show description dialog (Added)
    private void showDescriptionDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Optional: Handle back button to prevent going back to login screen easily
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Close app or manage navigation stack as needed
    }
}