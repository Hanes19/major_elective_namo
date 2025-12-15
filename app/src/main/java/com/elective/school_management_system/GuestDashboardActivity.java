package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

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
        // The grid items are inside a GridLayout.
        // Based on g_dashboard.xml, we can identify them by their child index or by traversing.
        // However, the XML does not have IDs for the LinearLayout containers, only for inner contents.
        // We will traverse the GridLayout to find the clickable Linear Layouts.

        // Option B: Since we can't easily modify the XML IDs right now without re-uploading,
        // we can set OnClickListeners to the GridLayout's children by index.

        android.widget.GridLayout gridLayout = findViewById(R.id.searchContainer).getRootView().findViewById(R.id.searchContainer).getNextFocusDownId() != View.NO_ID
                ? findViewById(R.id.searchContainer).getRootView().findViewById(R.id.searchContainer) // This lookup is tricky without IDs.
                : null;

        // Simpler approach: Find the container and getting children.
        // Note: In g_dashboard.xml provided, the GridLayout is directly below @id/searchContainer

        // Let's assume you will ADD IDs to the LinearLayouts in g_dashboard.xml for robustness.
        // For this code, I will use findViewById assuming you update the XML or I will find them by traversing the parent ViewGroup.

        // Finding the GridLayout
        android.view.ViewGroup rootView = (android.view.ViewGroup) ((android.view.ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        android.widget.GridLayout grid = null;

        for(int i=0; i<rootView.getChildCount(); i++){
            if(rootView.getChildAt(i) instanceof android.widget.GridLayout){
                grid = (android.widget.GridLayout) rootView.getChildAt(i);
                break;
            }
        }

        if (grid != null) {
            // Child 0: Admissions
            grid.getChildAt(0).setOnClickListener(v -> startNavigation("Admissions Office"));

            // Child 1: Cashier
            grid.getChildAt(1).setOnClickListener(v -> startNavigation("Cashier"));

            // Child 2: Events
            grid.getChildAt(2).setOnClickListener(v -> startActivity(new Intent(this, GuestUpdatesActivity.class)));

            // Child 3: Restrooms
            grid.getChildAt(3).setOnClickListener(v -> startNavigation("Restroom"));
        }
    }

    private void setupSearchListener() {
        LinearLayout searchContainer = findViewById(R.id.searchContainer);
        searchContainer.setOnClickListener(v -> {
            Intent intent = new Intent(GuestDashboardActivity.this, StudentRoomsListActivity.class);
            // We can treat the room list as a general directory
            startActivity(intent);
        });
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