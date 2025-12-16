package com.elective.school_management_system;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AdminDashboardActivity extends AppCompatActivity {

    // Bottom Navigation Views
    private LinearLayout navMaps, navDashboard, navUpdates;

    // Management Console Buttons
    private ConstraintLayout btnManageMap, btnManageUsers, btnReports;

    // System Overview Cards
    private LinearLayout cardActiveUsers, cardNewReports, cardSysHealth;

    // Stats Text Views (Added)
    private TextView tvActiveUsersCount, tvNewReportsCount, tvSysHealthCount;

    // Settings Button
    private FrameLayout btnSettings;

    // Database Helper
    private DatabaseHelper dbHelper;

    // Live Stats Variables
    private int totalUsers = 0;
    private int studentCount = 0;
    private int guestCount = 0;
    private int pendingReports = 0;
    private String reportsBreakdown = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_dashboard);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh stats when returning to the dashboard
        updateDashboardStats();
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

        // System Overview Cards
        cardActiveUsers = findViewById(R.id.cardActiveUsers);
        cardNewReports = findViewById(R.id.cardNewReports);
        cardSysHealth = findViewById(R.id.cardSysHealth);

        // Stats TextViews
        tvActiveUsersCount = findViewById(R.id.tvActiveUsersCount);
        tvNewReportsCount = findViewById(R.id.tvNewReportsCount);
        tvSysHealthCount = findViewById(R.id.tvSysHealthCount);

        // Settings
        btnSettings = findViewById(R.id.btnSettings);
    }

    private void updateDashboardStats() {
        // Fetch data from Database
        totalUsers = dbHelper.getTotalUserCount();
        studentCount = dbHelper.getStudentCount();
        guestCount = totalUsers - studentCount; // Assuming everyone else is Guest/Staff

        pendingReports = dbHelper.getPendingReportsCount();
        reportsBreakdown = dbHelper.getPendingReportBreakdown();

        // Update UI
        tvActiveUsersCount.setText(String.valueOf(totalUsers));
        tvNewReportsCount.setText(String.valueOf(pendingReports));

        // Randomize System Health slightly for effect (since it's a mock metric)
        int health = 98 + (int)(Math.random() * 3) - 1; // Random between 97-100
        tvSysHealthCount.setText(health + "%");
    }

    private void setupListeners() {
        // --- System Overview Listeners (Updated) ---

        cardActiveUsers.setOnClickListener(v -> {
            String breakdown = "Total Users: " + totalUsers + "\n\n" +
                    "• Students: " + studentCount + "\n" +
                    "• Staff/Guests: " + guestCount;
            showDescriptionDialog("Active Users Breakdown", breakdown);
        });

        cardNewReports.setOnClickListener(v -> {
            String msg = "Pending Reports: " + pendingReports + "\n\n" +
                    "Breakdown by Category:\n" + reportsBreakdown;
            showDescriptionDialog("Report Statistics", msg);
        });

        cardSysHealth.setOnClickListener(v -> {
            // Mock breakdown for system health
            String healthStats = "Overall Stability: " + tvSysHealthCount.getText() + "\n\n" +
                    "• Database: Online (5ms latency)\n" +
                    "• Server Load: 12% (Normal)\n" +
                    "• Storage: 45% Used";
            showDescriptionDialog("System Health Diagnostics", healthStats);
        });


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

    // Helper method to show description dialog
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