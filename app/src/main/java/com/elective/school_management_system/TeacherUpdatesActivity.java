package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherUpdatesActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private LinearLayout navMaps, navDashboard, navUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_update_list);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        // Find the bottom nav items directly by their new IDs
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Navigation Logic
        navDashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherDashboardActivity.class));
            finish();
        });

        navMaps.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherRoomsActivity.class));
            finish();
        });

        // navUpdates is the current activity, so no listener needed or just return
    }
}