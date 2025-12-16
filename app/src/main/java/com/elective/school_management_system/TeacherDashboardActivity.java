package com.elective.school_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

    // Views
    private LinearLayout navMaps, navDashboard, navUpdates;
    private LinearLayout btnAssignedRooms, btnMySchedule;
    private LinearLayout btnNavFaculty, btnNavAdmin, btnNavClinic;
    private FrameLayout imgLogo;

    // Dynamic Data Views
    private TextView tvWelcome, tvUpcomingSubject, tvUpcomingLocation, tvUpcomingTime;
    private TextView tabSchedule, tabRequests;
    private View cardUpcoming;
    private View layoutRequests; // New View

    private DatabaseHelper dbHelper;
    private int userId;
    private String userEmail;

    // School Coordinates (TS Building, Hagkol, Valencia City)
    private static final double SCHOOL_LAT = 7.9230;
    private static final double SCHOOL_LNG = 125.0953;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_dashboard);

        dbHelper = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = prefs.getString("email", "");
        userId = dbHelper.getUserId(userEmail);

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
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        btnAssignedRooms = findViewById(R.id.btnAssignedRooms);
        btnMySchedule = findViewById(R.id.btnMySchedule);

        btnNavFaculty = findViewById(R.id.btnNavFaculty);
        btnNavAdmin = findViewById(R.id.btnNavAdmin);
        btnNavClinic = findViewById(R.id.btnNavClinic);

        imgLogo = findViewById(R.id.imgLogo);
        tvWelcome = findViewById(R.id.tvWelcome);

        tvUpcomingSubject = findViewById(R.id.tvUpcomingSubject);
        tvUpcomingLocation = findViewById(R.id.tvUpcomingLocation);
        tvUpcomingTime = findViewById(R.id.tvUpcomingTime);

        tabSchedule = findViewById(R.id.tabSchedule);
        tabRequests = findViewById(R.id.tabRequests);
        cardUpcoming = findViewById(R.id.cardUpcoming);
        layoutRequests = findViewById(R.id.layoutRequests); // Bind new view
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

        btnAssignedRooms.setOnClickListener(v -> startActivity(new Intent(this, TeacherAssignedRoomsActivity.class)));
        btnMySchedule.setOnClickListener(v -> startActivity(new Intent(this, TeacherScheduleActivity.class)));

        // Quick Navigation (Uses Google Maps)
        btnNavFaculty.setOnClickListener(v -> openGoogleMaps());
        btnNavAdmin.setOnClickListener(v -> openGoogleMaps());
        btnNavClinic.setOnClickListener(v -> openGoogleMaps());

        // Tab Logic
        tabSchedule.setOnClickListener(v -> {
            updateTabUI(true);
            cardUpcoming.setVisibility(View.VISIBLE);
            layoutRequests.setVisibility(View.GONE);
        });

        tabRequests.setOnClickListener(v -> {
            updateTabUI(false);
            cardUpcoming.setVisibility(View.GONE);
            layoutRequests.setVisibility(View.VISIBLE);
        });

        imgLogo.setOnClickListener(v -> showProfileDialog());
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

    private void openGoogleMaps() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + SCHOOL_LAT + "," + SCHOOL_LNG);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProfileDialog() {
        String username = dbHelper.getUsername(userEmail);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Teacher Profile");
        builder.setMessage("Name: " + username + "\nEmail: " + userEmail + "\nRole: Instructor");

        builder.setPositiveButton("Logout", (dialog, which) -> logoutUser());
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