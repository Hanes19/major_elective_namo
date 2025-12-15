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
            // Navigate to Map Management (e.g., AdminManageRoomsActivity)
            // Intent intent = new Intent(AdminDashboardActivity.this, AdminManageRoomsActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Manage Map Data clicked", Toast.LENGTH_SHORT).show();
        });

        btnManageUsers.setOnClickListener(v -> {
            // Navigate to User Directory (e.g., AdminUserListActivity)
            // Intent intent = new Intent(AdminDashboardActivity.this, AdminUserListActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "User Directory clicked", Toast.LENGTH_SHORT).show();
        });

        btnReports.setOnClickListener(v -> {
            // Navigate to Reports (e.g., AdminReportsActivity)
            // Intent intent = new Intent(AdminDashboardActivity.this, AdminReportsActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "System Reports clicked", Toast.LENGTH_SHORT).show();
        });

        // --- Bottom Navigation Listeners ---

        navMaps.setOnClickListener(v -> {
            // Navigate to Map/Rooms View
            Toast.makeText(this, "Maps Navigation clicked", Toast.LENGTH_SHORT).show();
            // Example: startActivity(new Intent(this, StudentRoomMapActivity.class));
        });

        navDashboard.setOnClickListener(v -> {
            // Already on Dashboard
            Toast.makeText(this, "You are on the Dashboard", Toast.LENGTH_SHORT).show();
        });

        navUpdates.setOnClickListener(v -> {
            // Navigate to Updates/Reports View
            Toast.makeText(this, "Updates Navigation clicked", Toast.LENGTH_SHORT).show();
        });
    }

    // Optional: Handle back button to prevent going back to login screen easily
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Close app or manage navigation stack as needed
    }
}