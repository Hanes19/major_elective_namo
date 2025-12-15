package com.elective.school_management_system;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NavSettingsActivity extends AppCompatActivity {

    // UI Components matching your XML IDs
    private Switch switchScreenOn, switchSmoothing;
    private LinearLayout btnCalibrate;
    private Button btnLogout;
    private ImageView btnBack;

    // SharedPreferences for saving data
    private SharedPreferences navPrefs; // For navigation settings
    private SharedPreferences sessionPrefs; // For login session

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make sure this matches your XML file name
        setContentView(R.layout.ad_nav_settings);

        // 1. Initialize SharedPreferences
        // "NavPrefs" will store settings specific to the navigation feature
        navPrefs = getSharedPreferences("NavPrefs", Context.MODE_PRIVATE);
        // "UserSession" (or whatever you use for login) stores the user's login state
        sessionPrefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        initViews();
        loadSavedSettings();
        setupListeners();
    }

    private void initViews() {
        switchScreenOn = findViewById(R.id.switchScreenOn);
        switchSmoothing = findViewById(R.id.switchSmoothing);
        btnCalibrate = findViewById(R.id.btnCalibrate);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadSavedSettings() {
        // Load "Keep Screen On" (Default to true if not set)
        boolean isScreenOnEnabled = navPrefs.getBoolean("keep_screen_on", true);
        switchScreenOn.setChecked(isScreenOnEnabled);

        // Load "Compass Smoothing" (Default to true if not set)
        boolean isSmoothingEnabled = navPrefs.getBoolean("compass_smoothing", true);
        switchSmoothing.setChecked(isSmoothingEnabled);
    }

    private void setupListeners() {
        // --- Back Button ---
        btnBack.setOnClickListener(v -> finish());

        // --- Keep Screen On Switch ---
        switchScreenOn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = navPrefs.edit();
            editor.putBoolean("keep_screen_on", isChecked);
            editor.apply();
        });

        // --- Compass Smoothing Switch ---
        switchSmoothing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = navPrefs.edit();
            editor.putBoolean("compass_smoothing", isChecked);
            editor.apply();

            String status = isChecked ? "Enabled" : "Disabled";
            Toast.makeText(this, "Stabilization " + status, Toast.LENGTH_SHORT).show();
        });

        // --- Calibrate Button ---
        btnCalibrate.setOnClickListener(v -> showCalibrationDialog());

        // --- Logout Button ---
        btnLogout.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void showCalibrationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Calibrate Sensors")
                .setMessage("To calibrate your compass, hold your phone and move it in a figure-8 motion for a few seconds.")
                .setPositiveButton("Got it", null)
                .setIcon(android.R.drawable.ic_menu_compass)
                .show();
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to end your session?")
                .setPositiveButton("Log Out", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performLogout() {
        // 1. Clear Session Data
        SharedPreferences.Editor editor = sessionPrefs.edit();
        editor.clear(); // This removes all saved data in this file
        editor.apply();

        // 2. Navigate back to Login Activity
        Intent intent = new Intent(NavSettingsActivity.this, Login_Activity.class);
        // Clear the back stack so the user cannot press "Back" to return to settings
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // 3. Close this activity
        finish();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}