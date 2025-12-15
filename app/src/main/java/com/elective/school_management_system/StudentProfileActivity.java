package com.elective.school_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentProfileActivity extends AppCompatActivity {

    private ImageButton btnBack, btnEdit;
    private TextView btnLogout;
    private TextView tvName, tvEmail;

    // Added: Bottom Navigation Layouts
    private LinearLayout navHome, navNav, navProfile;

    private DatabaseHelper dbHelper;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_profile);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadUserProfile();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile(); // Refresh data when coming back
    }

    private void initViews() {
        // Matches XML types
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogout);

        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);

        // Initialize Bottom Navigation Views
        navHome = findViewById(R.id.navHome);
        navNav = findViewById(R.id.navNav);
        navProfile = findViewById(R.id.navProfile);
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentEmail = prefs.getString("email", "");

        if (currentEmail.isEmpty()) {
            return;
        }

        if (tvEmail != null) tvEmail.setText(currentEmail);
        String name = dbHelper.getUsername(currentEmail);
        if (tvName != null) tvName.setText(name);
    }

    private void setupListeners() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> logout());
        }

        // EDIT BUTTON LOGIC
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(StudentProfileActivity.this, StudentEditProfileActivity.class);
                startActivity(intent);
            });
        }

        // --- Bottom Navigation Logic ---

        // 1. Home Clicked -> Go to StudentDashboardActivity
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(StudentProfileActivity.this, StudentDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // Close Profile so it doesn't pile up in back stack
            });
        }

        // 2. Navigation Clicked -> Go to StudentNavigationListActivity
        if (navNav != null) {
            navNav.setOnClickListener(v -> {
                Intent intent = new Intent(StudentProfileActivity.this, StudentNavigationListActivity.class);
                startActivity(intent);
                finish(); // Close Profile
            });
        }

        // 3. Profile Clicked -> Already here
        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                // Already on profile, do nothing or just refresh
            });
        }
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(StudentProfileActivity.this, Login_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}