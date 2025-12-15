package com.elective.school_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_dashboard);

        dbHelper = new DatabaseHelper(this);

        // Load Session
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        userId = dbHelper.getUserId(email);

        initViews();
        loadUserData(email);
        loadUpcomingClass();
        setupListeners();
    }

    private void initViews() {
        // Navigation
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        // Buttons
        btnAssignedRooms = findViewById(R.id.btnAssignedRooms);
        btnMySchedule = findViewById(R.id.btnMySchedule);
        btnNavFaculty = findViewById(R.id.btnNavFaculty);
        btnNavAdmin = findViewById(R.id.btnNavAdmin);
        btnNavClinic = findViewById(R.id.btnNavClinic);
        imgLogo = findViewById(R.id.imgLogo);

        // Dynamic Texts
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUpcomingSubject = findViewById(R.id.tvUpcomingSubject);
        tvUpcomingLocation = findViewById(R.id.tvUpcomingLocation);
        tvUpcomingTime = findViewById(R.id.tvUpcomingTime);

        // Tabs
        tabSchedule = findViewById(R.id.tabSchedule);
        tabRequests = findViewById(R.id.tabRequests);
        cardUpcoming = findViewById(R.id.cardUpcoming);
    }

    private void loadUserData(String email) {
        String username = dbHelper.getUsername(email);
        if (username != null && !username.isEmpty()) {
            tvWelcome.setText("Welcome, " + username + "!");
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
        // Bottom Navigation
        navMaps.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherRoomsActivity.class));
            // Don't finish() here if you want to keep the dashboard in stack,
            // or finish() if you want a flat navigation hierarchy.
        });
        navUpdates.setOnClickListener(v -> startActivity(new Intent(this, TeacherUpdatesActivity.class)));

        // Main Features
        btnAssignedRooms.setOnClickListener(v -> startActivity(new Intent(this, TeacherAssignedRoomsActivity.class)));
        btnMySchedule.setOnClickListener(v -> startActivity(new Intent(this, TeacherScheduleActivity.class)));

        // Quick Nav
        btnNavFaculty.setOnClickListener(v -> openNavigation("Faculty Room"));
        btnNavAdmin.setOnClickListener(v -> openNavigation("Admin Office"));
        btnNavClinic.setOnClickListener(v -> openNavigation("Clinic"));

        // Tabs Logic
        tabSchedule.setOnClickListener(v -> {
            updateTabUI(true);
            cardUpcoming.setVisibility(View.VISIBLE);
            // If you had a requests layout, hide it here
        });

        tabRequests.setOnClickListener(v -> {
            updateTabUI(false);
            cardUpcoming.setVisibility(View.GONE);
            // If you had a requests layout, show it here
            Toast.makeText(this, "No pending requests", Toast.LENGTH_SHORT).show();
        });

        // Profile
        imgLogo.setOnClickListener(v -> Toast.makeText(this, "Teacher Profile", Toast.LENGTH_SHORT).show());
    }

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

    private void openNavigation(String roomName) {
        Intent intent = new Intent(this, TeacherARNavigationActivity.class);
        intent.putExtra("ROOM_NAME", roomName);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUpcomingClass(); // Refresh data when returning
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}