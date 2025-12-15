package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

    private LinearLayout navMaps, navDashboard, navUpdates;
    private LinearLayout btnAssignedRooms, btnMySchedule;
    private LinearLayout btnNavFaculty, btnNavAdmin, btnNavClinic;
    private FrameLayout imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_dashboard);

        initViews();
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
    }

    private void setupListeners() {
        // Bottom Navigation
        navMaps.setOnClickListener(v -> startActivity(new Intent(this, TeacherRoomsActivity.class)));
        navUpdates.setOnClickListener(v -> startActivity(new Intent(this, TeacherUpdatesActivity.class)));

        // Main Features
        btnAssignedRooms.setOnClickListener(v -> startActivity(new Intent(this, TeacherAssignedRoomsActivity.class)));

        // --- UPDATED: Schedule Functionality ---
        btnMySchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherScheduleActivity.class);
            startActivity(intent);
        });

        // Quick Nav
        btnNavFaculty.setOnClickListener(v -> openNavigation("Faculty Room"));
        btnNavAdmin.setOnClickListener(v -> openNavigation("Admin Office"));
        btnNavClinic.setOnClickListener(v -> openNavigation("Clinic"));

        // Profile
        imgLogo.setOnClickListener(v -> {
            // Can be linked to a TeacherProfileActivity if desired
            Toast.makeText(this, "Teacher Profile", Toast.LENGTH_SHORT).show();
        });
    }

    private void openNavigation(String roomName) {
        Intent intent = new Intent(this, TeacherARNavigationActivity.class);
        intent.putExtra("ROOM_NAME", roomName);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}