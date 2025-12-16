package com.elective.school_management_system;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

    // Dynamic Data Views
    private TextView tvWelcome, tvUpcomingSubject, tvUpcomingLocation, tvUpcomingTime;
    private TextView tabSchedule, tabRequests;
    private View cardUpcoming; // To toggle visibility

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

        // --- Quick AR Navigation (With Permission Check) ---
        btnNavFaculty.setOnClickListener(v -> checkCameraPermissionAndOpen("Faculty Room"));
        btnNavAdmin.setOnClickListener(v -> checkCameraPermissionAndOpen("Admin Office"));
        btnNavClinic.setOnClickListener(v -> checkCameraPermissionAndOpen("Clinic"));

        // --- Tabs Logic ---
        tabSchedule.setOnClickListener(v -> {
            updateTabUI(true);
            cardUpcoming.setVisibility(View.VISIBLE);
        });

        tabRequests.setOnClickListener(v -> {
            updateTabUI(false);
            cardUpcoming.setVisibility(View.GONE);
            // Feature placeholder
            Toast.makeText(this, "No pending requests at the moment.", Toast.LENGTH_SHORT).show();
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

        // Added: Navigate to Edit Profile
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
        loadUpcomingClass(); // Refresh data in case schedule changed
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Exit app
    }
}