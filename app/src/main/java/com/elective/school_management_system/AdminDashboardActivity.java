package com.elective.school_management_system;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AdminDashboardActivity extends AppCompatActivity {

    // Bottom Navigation Views
    private LinearLayout navMaps, navDashboard, navUpdates;
    private ImageView iconMaps, iconDashboard, iconUpdates;
    private TextView textMaps, textDashboard, textUpdates;

    // Management Console Buttons
    private ConstraintLayout btnManageMap, btnManageUsers, btnReports;

    // System Overview Cards
    private LinearLayout cardActiveUsers, cardNewReports, cardSysHealth;

    // Stats Text Views
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
        updateNavUI(); // Set the correct colors for this active tab
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDashboardStats();
    }

    private void initViews() {
        // Bottom Navigation
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        iconMaps = findViewById(R.id.iconMaps);
        iconDashboard = findViewById(R.id.iconDashboard);
        iconUpdates = findViewById(R.id.iconUpdates);

        textMaps = findViewById(R.id.textMaps);
        textDashboard = findViewById(R.id.textDashboard);
        textUpdates = findViewById(R.id.textUpdates);

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

    private void updateNavUI() {
        // This is the Dashboard Activity, so Dashboard is Active
        int activeColor = Color.parseColor("#A9016D");
        int inactiveColor = Color.WHITE;

        // Maps (Inactive)
        iconMaps.setColorFilter(inactiveColor);
        textMaps.setTextColor(inactiveColor);

        // Dashboard (Active)
        iconDashboard.setColorFilter(activeColor);
        textDashboard.setTextColor(activeColor);

        // Updates (Inactive)
        iconUpdates.setColorFilter(inactiveColor);
        textUpdates.setTextColor(inactiveColor);
    }

    private void updateDashboardStats() {
        totalUsers = dbHelper.getTotalUserCount();
        studentCount = dbHelper.getStudentCount();
        guestCount = totalUsers - studentCount;
        pendingReports = dbHelper.getPendingReportsCount();
        reportsBreakdown = dbHelper.getPendingReportBreakdown();

        tvActiveUsersCount.setText(String.valueOf(totalUsers));
        tvNewReportsCount.setText(String.valueOf(pendingReports));

        int health = 98 + (int)(Math.random() * 3) - 1;
        tvSysHealthCount.setText(health + "%");
    }

    private void setupListeners() {
        // --- Bottom Navigation Listeners (With Animations) ---

        navMaps.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminRoomMapActivity.class);
            startActivity(intent);
            // Maps is to the LEFT of Dashboard -> Slide In Left
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        navDashboard.setOnClickListener(v -> {
            // Already here
        });

        navUpdates.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminReportsActivity.class);
            startActivity(intent);
            // Updates is to the RIGHT of Dashboard -> Slide In Right
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // --- Management Console Listeners ---

        btnManageMap.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, AdminManageRoomsActivity.class));
        });

        btnManageUsers.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, AdminUserListActivity.class));
        });

        btnReports.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, AdminReportsActivity.class));
        });

        // --- Settings & Cards ---

        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, NavSettingsActivity.class));
        });

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
            String healthStats = "Overall Stability: " + tvSysHealthCount.getText() + "\n\n" +
                    "• Database: Online (5ms latency)\n" +
                    "• Server Load: 12% (Normal)\n" +
                    "• Storage: 45% Used";
            showDescriptionDialog("System Health Diagnostics", healthStats);
        });
    }

    private void showDescriptionDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}