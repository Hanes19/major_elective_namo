package com.elective.school_management_system;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class GuestDashboardActivity extends AppCompatActivity {

    // School Coordinates (TS Building, Hagkol, Valencia City)
    private static final double SCHOOL_LAT = 7.9230;
    private static final double SCHOOL_LNG = 125.0953;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_dashboard);

        setupGridListeners();
        setupSearchListener();
    }

    private void setupGridListeners() {
        android.view.ViewGroup rootView = (android.view.ViewGroup) ((android.view.ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        android.widget.GridLayout grid = null;

        for(int i=0; i<rootView.getChildCount(); i++){
            if(rootView.getChildAt(i) instanceof android.widget.GridLayout){
                grid = (android.widget.GridLayout) rootView.getChildAt(i);
                break;
            }
        }

        if (grid != null) {
            // Navigation features now open Google Maps
            grid.getChildAt(0).setOnClickListener(v -> openGoogleMaps()); // Admissions
            grid.getChildAt(1).setOnClickListener(v -> openGoogleMaps()); // Cashier

            grid.getChildAt(2).setOnClickListener(v -> {
                startActivity(new Intent(this, GuestUpdatesActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });

            grid.getChildAt(3).setOnClickListener(v -> openGoogleMaps()); // Restrooms
        }
    }

    private void setupSearchListener() {
        LinearLayout searchContainer = findViewById(R.id.searchContainer);
        searchContainer.setOnClickListener(v -> {
            Intent intent = new Intent(GuestDashboardActivity.this, StudentRoomsListActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        View mapBanner = findViewById(R.id.mapBanner);
        if (mapBanner != null) {
            mapBanner.setOnClickListener(v -> {
                Intent intent = new Intent(GuestDashboardActivity.this, GuestMapActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
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
}