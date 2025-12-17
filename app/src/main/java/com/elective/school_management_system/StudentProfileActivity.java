package com.elective.school_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudentProfileActivity extends AppCompatActivity {

    private ImageButton btnBack, btnEdit;
    private TextView btnLogout;
    private TextView tvName, tvEmail;

    // Settings Menu Buttons
    private LinearLayout btnChangePassword, btnUpdates, btnHelpSupport;

    // Bottom Navigation
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
        loadUserProfile();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);

        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);

        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnUpdates = findViewById(R.id.btnUpdates);
        btnHelpSupport = findViewById(R.id.btnHelpSupport);
        btnLogout = findViewById(R.id.btnLogout);

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
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Profile Editing
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(StudentProfileActivity.this, StudentEditProfileActivity.class);
                startActivity(intent);
            });
        }

        if (btnLogout != null) btnLogout.setOnClickListener(v -> logout());

        // --- Settings Functionality ---

        // 1. Change Password / Settings
        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(v -> {
                Intent intent = new Intent(StudentProfileActivity.this, StudentResetPasswordActivity.class);
                startActivity(intent);
            });
        }

        // 2. Updates
        if (btnUpdates != null) {
            btnUpdates.setOnClickListener(v -> {
                Intent intent = new Intent(StudentProfileActivity.this, GuestUpdatesActivity.class);
                startActivity(intent);
            });
        }

        // 3. Help & Support
        if (btnHelpSupport != null) {
            btnHelpSupport.setOnClickListener(v -> {
                Intent intent = new Intent(StudentProfileActivity.this, StudentHelpSupportActivity.class);
                startActivity(intent);
            });
        }

        // --- Bottom Navigation ---
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(StudentProfileActivity.this, StudentDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                // Animate Left
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            });
        }

        if (navNav != null) {
            navNav.setOnClickListener(v -> {
                Intent intent = new Intent(StudentProfileActivity.this, StudentNavigationListActivity.class);
                startActivity(intent);
                finish();
                // Animate Left
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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