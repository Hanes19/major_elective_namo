package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class TeacherDashboardActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 101;

    // Views
    private LinearLayout navMaps, navDashboard, navUpdates;
    private LinearLayout btnAssignedRooms, btnMySchedule;
    private LinearLayout btnNavFaculty, btnNavAdmin, btnNavClinic;
    private FrameLayout imgLogo;

    // Stats Containers
    private LinearLayout containerEnrollmentStats, containerSectionStats;

    // Dynamic Data Views
    private TextView tvWelcome, tvUpcomingSubject, tvUpcomingLocation, tvUpcomingTime;
    private TextView tabSchedule, tabRequests;
    private View cardUpcoming; // To toggle visibility
    private LinearLayout layoutRequests; // Added missing reference for the requests view

    private DatabaseHelper dbHelper;
    private int userId;
    private String userEmail;
    private String pendingRoomName; // For permission handling

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_dashboard);

        dbHelper = new DatabaseHelper(this);

        // Load Session
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = prefs.getString("email", "");
        userId = dbHelper.getUserId(userEmail);

        // If no session, redirect to Login
        if (userId == -1 || userEmail.isEmpty()) {
            logoutUser();
            return;
        }

        initViews();
        loadUserData();
        loadUpcomingClass();
        loadDashboardStats();
        setupListeners();
    }

    private void initViews() {
        // Navigation
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        // Main Feature Buttons
        btnAssignedRooms = findViewById(R.id.btnAssignedRooms);
        btnMySchedule = findViewById(R.id.btnMySchedule);

        // Quick Nav Buttons
        btnNavFaculty = findViewById(R.id.btnNavFaculty);
        btnNavAdmin = findViewById(R.id.btnNavAdmin);
        btnNavClinic = findViewById(R.id.btnNavClinic);

        // Header & Profile
        imgLogo = findViewById(R.id.imgLogo);
        tvWelcome = findViewById(R.id.tvWelcome);

        // Upcoming Class Data
        tvUpcomingSubject = findViewById(R.id.tvUpcomingSubject);
        tvUpcomingLocation = findViewById(R.id.tvUpcomingLocation);
        tvUpcomingTime = findViewById(R.id.tvUpcomingTime);

        // Tabs & Card
        tabSchedule = findViewById(R.id.tabSchedule);
        tabRequests = findViewById(R.id.tabRequests);
        cardUpcoming = findViewById(R.id.cardUpcoming);
        layoutRequests = findViewById(R.id.layoutRequests); // Initialize this view

        // Stats Containers
        containerEnrollmentStats = findViewById(R.id.containerEnrollmentStats);
        containerSectionStats = findViewById(R.id.containerSectionStats);
    }

    private void loadUserData() {
        String username = dbHelper.getUsername(userEmail);
        if (username != null && !username.isEmpty()) {
            tvWelcome.setText("Welcome, " + username + "!");
        } else {
            tvWelcome.setText("Welcome, Teacher!");
        }
    }

    private void loadUpcomingClass() {
        Schedule nextClass = dbHelper.getNextClass(userId);
        if (nextClass != null) {
            tvUpcomingSubject.setText(nextClass.getSubject());
            tvUpcomingLocation.setText(nextClass.getRoomName());
            tvUpcomingTime.setText(nextClass.getStartTime() + " - " + nextClass.getEndTime());
        } else {
            tvUpcomingSubject.setText("No upcoming classes");
            tvUpcomingLocation.setText("--");
            tvUpcomingTime.setText("--");
        }
    }

    private void loadDashboardStats() {
        if(containerEnrollmentStats == null || containerSectionStats == null) return;

        // Clear containers first
        containerEnrollmentStats.removeAllViews();
        containerSectionStats.removeAllViews();

        // MOCK DATA: Enrollment Per Subject
        addStatRow(containerEnrollmentStats, "Mathematics 101", "42 Students");
        addStatRow(containerEnrollmentStats, "Physics 201", "35 Students");
        addStatRow(containerEnrollmentStats, "History 101", "50 Students");

        // MOCK DATA: Students Per Section
        addStatRow(containerSectionStats, "Grade 10 - Sec A", "30 Students");
        addStatRow(containerSectionStats, "Grade 10 - Sec B", "28 Students");
        addStatRow(containerSectionStats, "Grade 11 - Sec A", "32 Students");
    }

    private void addStatRow(LinearLayout container, String label, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0, 12, 0, 12);
        row.setLayoutParams(rowParams);

        TextView tvLabel = new TextView(this);
        tvLabel.setText(label);
        tvLabel.setTextSize(14);
        tvLabel.setTextColor(Color.parseColor("#E0E0E0")); // Light Grey
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        tvLabel.setLayoutParams(labelParams);

        TextView tvValue = new TextView(this);
        tvValue.setText(value);
        tvValue.setTextSize(14);
        tvValue.setTypeface(null, Typeface.BOLD);
        tvValue.setTextColor(Color.WHITE); // White for emphasis

        row.addView(tvLabel);
        row.addView(tvValue);

        // Divider
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(Color.parseColor("#1AFFFFFF")); // Very subtle line

        container.addView(row);
        container.addView(divider);
    }

    private void setupListeners() {
        // --- Bottom Navigation ---
        navMaps.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherRoomsActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        navUpdates.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherUpdatesActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // --- Dashboard Buttons ---
        btnAssignedRooms.setOnClickListener(v -> startActivity(new Intent(this, TeacherAssignedRoomsActivity.class)));
        btnMySchedule.setOnClickListener(v -> startActivity(new Intent(this, TeacherScheduleActivity.class)));

        // --- Quick Nav Buttons ---
        btnNavFaculty.setOnClickListener(v -> checkCameraPermissionAndOpen("Faculty Room"));
        btnNavAdmin.setOnClickListener(v -> checkCameraPermissionAndOpen("Admin Office"));
        btnNavClinic.setOnClickListener(v -> checkCameraPermissionAndOpen("Clinic"));

        // --- Upcoming Class Click Listener (Moved here to fix NullPointerException) ---
        if(cardUpcoming != null) {
            cardUpcoming.setOnClickListener(v -> {
                String room = (tvUpcomingLocation != null) ? tvUpcomingLocation.getText().toString() : "";
                if(room != null && !room.equals("--") && !room.isEmpty()) {
                    checkCameraPermissionAndOpen(room);
                } else {
                    Toast.makeText(this, "No upcoming class location", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // --- Tabs Logic ---
        tabSchedule.setOnClickListener(v -> {
            updateTabUI(true);
            if(cardUpcoming != null) cardUpcoming.setVisibility(View.VISIBLE);
            if(layoutRequests != null) layoutRequests.setVisibility(View.GONE);
        });

        tabRequests.setOnClickListener(v -> {
            updateTabUI(false);
            if(cardUpcoming != null) cardUpcoming.setVisibility(View.GONE);
            if(layoutRequests != null) layoutRequests.setVisibility(View.VISIBLE);
        });

        // --- Profile / Logout ---
        imgLogo.setOnClickListener(v -> showProfileDialog());
    }

    // --- UI Helpers ---
    private void updateTabUI(boolean isScheduleActive) {
        if (isScheduleActive) {
            tabSchedule.setBackgroundResource(R.drawable.glass_tab_active);
            tabSchedule.setTextColor(Color.WHITE);
            tabRequests.setBackground(null);
            tabRequests.setTextColor(Color.parseColor("#B0B0B0"));
        } else {
            tabRequests.setBackgroundResource(R.drawable.glass_tab_active);
            tabRequests.setTextColor(Color.WHITE);
            tabSchedule.setBackground(null);
            tabSchedule.setTextColor(Color.parseColor("#B0B0B0"));
        }
    }

    // --- AR Navigation & Permissions ---
    private void checkCameraPermissionAndOpen(String roomName) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            pendingRoomName = roomName;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openNavigation(roomName);
        }
    }

    private void openNavigation(String roomName) {
        Intent intent = new Intent(this, TeacherARNavigationActivity.class);
        intent.putExtra("TARGET_ROOM", roomName);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String roomToOpen = (pendingRoomName != null) ? pendingRoomName : "Navigation";
                openNavigation(roomToOpen);
            } else {
                Toast.makeText(this, "Camera permission is required for AR Navigation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // --- Profile & Logout Logic ---
    private void showProfileDialog() {
        String username = dbHelper.getUsername(userEmail);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Teacher Profile");
        builder.setMessage("Name: " + username + "\nEmail: " + userEmail + "\nRole: Instructor");

        builder.setPositiveButton("Logout", (dialog, which) -> logoutUser());

        builder.setNeutralButton("Edit Profile", (dialog, which) -> {
            Intent intent = new Intent(TeacherDashboardActivity.this, TeacherEditProfileActivity.class);
            startActivity(intent);
        });

        builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, Login_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUpcomingClass();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}