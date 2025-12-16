package com.elective.school_management_system;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class TeacherRoomsActivity extends AppCompatActivity {

    private ImageButton btnBack, btnSearch;
    private AppCompatButton btnGetDirections; // The button in the bottom sheet

    // School Coordinates
    private static final double SCHOOL_LAT = 14.5995;
    private static final double SCHOOL_LNG = 120.9842;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_rooms);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);

        // Corrected: Find button by ID directly.
        // The previous code used findViewWithTag("directions") which didn't exist in t_rooms.xml
        btnGetDirections = findViewById(R.id.btnDirections_added_by_you);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v ->
                Toast.makeText(this, "Search Map...", Toast.LENGTH_SHORT).show());

        // Launch Google Maps instead of AR Navigation
        if(btnGetDirections != null) {
            btnGetDirections.setOnClickListener(v -> {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + SCHOOL_LAT + "," + SCHOOL_LNG);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}