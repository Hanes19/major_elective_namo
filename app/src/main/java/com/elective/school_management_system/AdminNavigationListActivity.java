package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AdminNavigationListActivity extends AppCompatActivity {

    // UI Components
    private ConstraintLayout btnManageMap, btnManageUsers, btnReports;
    private LinearLayout navMaps, navDashboard, navUpdates;
    private View btnSettings; // We will need to find this by walking the hierarchy or adding an ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_navigation_list);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnManageMap = findViewById(R.id.btnManageMap);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnReports = findViewById(R.id.btnReports);

        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        // Note: The settings icon in your XML didn't have an ID.
        // You should add android:id="@+id/btnSettings" to the FrameLayout wrapping the settings icon in ad_dashboard.xml
        // For now, we can try to find it, or you can add the ID.
        // btnSettings = findViewById(R.id.btnSettings);
    }

    private void setupListeners() {
        // --- Dashboard Cards ---

        // 1. Manage Map Data
        btnManageMap.setOnClickListener(v -> {
            // TODO: Create AdminManageRoomsActivity and link it here
            // Intent intent = new Intent(AdminDashboardActivity.this, AdminManageRoomsActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Manage Rooms/Map feature coming soon", Toast.LENGTH_SHORT).show();
        });

        // 2. User Directory (Linked to Instructors for now)
        btnManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminNavigationListActivity.this, AdminManageInstructorsActivity.class);
            startActivity(intent);
        });

        // 3. System Reports
        btnReports.setOnClickListener(v -> {
            Toast.makeText(this, "Reports feature coming soon", Toast.LENGTH_SHORT).show();
        });

        // --- Bottom Navigation ---

        navMaps.setOnClickListener(v -> {
            // Navigation logic if you want to reuse the Student Map or a specific Admin Map
            Toast.makeText(this, "Map View", Toast.LENGTH_SHORT).show();
        });

        navDashboard.setOnClickListener(v -> {
            // Already on Dashboard
        });

        navUpdates.setOnClickListener(v -> {
            Toast.makeText(this, "Updates View", Toast.LENGTH_SHORT).show();
        });

        // --- Logout Logic (Optional) ---
        // If you add the ID to the settings button, use this:
        /*
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                // Clear Session
                SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

                // Redirect to Login
                Intent intent = new Intent(AdminDashboardActivity.this, Login_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
        */
    }
}