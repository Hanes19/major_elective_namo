package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GuestDashboardActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 101;
    private String pendingNavigationTarget = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_dashboard);

        setupGridListeners();
        setupSearchListener();
    }

    private void setupGridListeners() {
        // Traversing the GridLayout to set listeners dynamically
        android.view.ViewGroup rootView = (android.view.ViewGroup) ((android.view.ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        android.widget.GridLayout grid = null;

        // Find the GridLayout inside the layout
        for(int i=0; i<rootView.getChildCount(); i++){
            if(rootView.getChildAt(i) instanceof android.widget.GridLayout){
                grid = (android.widget.GridLayout) rootView.getChildAt(i);
                break;
            }
        }

        if (grid != null) {
            // Child 0: Admissions -> Navigation
            grid.getChildAt(0).setOnClickListener(v -> startNavigation("Admissions Office"));

            // Child 1: Cashier -> Navigation
            grid.getChildAt(1).setOnClickListener(v -> startNavigation("Cashier"));

            // Child 2: Events -> Open Updates Activity
            grid.getChildAt(2).setOnClickListener(v -> {
                startActivity(new Intent(this, GuestUpdatesActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });

            // Child 3: Restrooms -> Navigation
            grid.getChildAt(3).setOnClickListener(v -> startNavigation("Restroom"));
        }
    }

    private void setupSearchListener() {
        LinearLayout searchContainer = findViewById(R.id.searchContainer);
        searchContainer.setOnClickListener(v -> {
            Intent intent = new Intent(GuestDashboardActivity.this, StudentRoomsListActivity.class);
            startActivity(intent);
            // Forward Animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Map Banner Listener (from g_dashboard.xml)
        View mapBanner = findViewById(R.id.mapBanner);
        if (mapBanner != null) {
            mapBanner.setOnClickListener(v -> {
                // UPDATED: Points to GuestMapActivity instead of immediate AR navigation
                Intent intent = new Intent(GuestDashboardActivity.this, GuestMapActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }
    }

    private void startNavigation(String target) {
        pendingNavigationTarget = target;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            launchNavigation(target);
        }
    }

    private void launchNavigation(String target) {
        Intent intent = new Intent(GuestDashboardActivity.this, StudentNavigationActivity.class);
        intent.putExtra("ROOM_NAME", target);
        startActivity(intent);
        // Forward Animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingNavigationTarget != null) {
                    launchNavigation(pendingNavigationTarget);
                }
            } else {
                Toast.makeText(this, "Camera permission needed for navigation", Toast.LENGTH_SHORT).show();
            }
        }
    }
}