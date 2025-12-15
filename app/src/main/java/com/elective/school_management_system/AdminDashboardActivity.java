package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AdminDashboardActivity extends AppCompatActivity {

    // Bottom Navigation Views
    private LinearLayout navMaps, navDashboard, navUpdates;

    // Management Console Buttons
    private ConstraintLayout btnManageMap, btnManageUsers, btnReports;

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
    }

    private void setupListeners() {
        // --- Management Console Listeners ---

        btnManageMap.setOnClickListener(v -> {
            // Navigate to Map Management
            // Ensure AdminManageRoomsActivity is created and registered in AndroidManifest.xml
            Intent intent = new Intent(AdminDashboardActivity.this, AdminManageRoomsActivity.class);
            startActivity(intent);
        });

        btnManageUsers.setOnClickListener(v -> {
            // Navigate to User Directory
            // Ensure AdminUserListActivity is created and registered in AndroidManifest.xml
            Intent intent = new Intent(AdminDashboardActivity.this, AdminUserListActivity.class);
            startActivity(intent);
        });

        btnReports.setOnClickListener(v -> {
            // Navigate to Reports
            // Ensure AdminReportsActivity is created and registered in AndroidManifest.xml
            Intent intent = new Intent(AdminDashboardActivity.this, AdminReportsActivity.class);
            startActivity(intent);
        });

        // --- Bottom Navigation Listeners ---

        navMaps.setOnClickListener(v -> {
            // Navigate to Map/Rooms View
            // Linking to the existing StudentRoomMapActivity
            Intent intent = new Intent(AdminDashboardActivity.this, StudentRoomMapActivity.class);
            startActivity(intent);
        });

        navDashboard.setOnClickListener(v -> {
            // Already on Dashboard
            Toast.makeText(this, "You are already on the Dashboard", Toast.LENGTH_SHORT).show();
        });

        navUpdates.setOnClickListener(v -> {
            // Navigate to Updates/Reports View
            // Assuming this also goes to the Reports activity
            Intent intent = new Intent(AdminDashboardActivity.this, AdminReportsActivity.class);
            startActivity(intent);
        });
    }

    // Optional: Handle back button to prevent going back to login screen easily
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Close app or manage navigation stack as needed
    }
}