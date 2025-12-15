package com.elective.school_management_system;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NavSettingsActivity extends AppCompatActivity {

    private Switch switchScreenOn, switchSmoothing;
    private LinearLayout btnCalibrate;
    private ImageView btnBack;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_settings);

        // Initialize Shared Preferences
        prefs = getSharedPreferences("NavPrefs", Context.MODE_PRIVATE);

        initViews();
        loadPreferences();
        setupListeners();
    }

    private void initViews() {
        switchScreenOn = findViewById(R.id.switchScreenOn);
        switchSmoothing = findViewById(R.id.switchSmoothing);
        btnCalibrate = findViewById(R.id.btnCalibrate);
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadPreferences() {
        // Default Screen On to TRUE
        switchScreenOn.setChecked(prefs.getBoolean("keep_screen_on", true));
        // Default Smoothing to TRUE
        switchSmoothing.setChecked(prefs.getBoolean("compass_smoothing", true));
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        switchScreenOn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("keep_screen_on", isChecked).apply();
        });

        switchSmoothing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("compass_smoothing", isChecked).apply();
            Toast.makeText(this, isChecked ? "Arrow movement smoothed" : "Raw sensor data enabled", Toast.LENGTH_SHORT).show();
        });

        btnCalibrate.setOnClickListener(v -> showCalibrationDialog());
    }

    private void showCalibrationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Calibrate Compass")
                .setMessage("To calibrate your compass, wave your phone in a figure-8 motion for a few seconds.")
                .setPositiveButton("OK", null)
                .show();
    }
}