package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ClientDashboardActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private ImageView imgSettings;
    private LinearLayout searchContainer;
    private LinearLayout cardRooms, cardInstructors, cardNav, cardProfile;

    // Added to fix "Cannot find symbol" error
    private LinearLayout item1, item2, item3;

    // NEW: Add Bottom Nav variables to match your XML IDs
    private LinearLayout navHome, navNav, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_client_dashboard);

        initViews();
        setupListeners();

        // NOTE: The line below was calling a method 'replaceFragment' that didn't exist
        // in your new Activity-based logic. I have commented it out.
        // If you still need a default fragment, you must uncomment the replaceFragment method below.
        // replaceFragment(new RoomsFragment());
    }

    private void initViews() {
        imgSettings = findViewById(R.id.img_settings);
        searchContainer = findViewById(R.id.search_container);
        cardRooms = findViewById(R.id.card_rooms);
        cardInstructors = findViewById(R.id.card_instructors);
        cardNav = findViewById(R.id.card_nav);
        cardProfile = findViewById(R.id.card_profile);

        // Defined these variables so they don't cause errors
        item1 = findViewById(R.id.item_1);
        item2 = findViewById(R.id.item_2);
        item3 = findViewById(R.id.item_3);

        // NEW: Initialize Bottom Nav
        navHome = findViewById(R.id.navHome);
        navNav = findViewById(R.id.navNav);
        navProfile = findViewById(R.id.navProfile);
    }

    private void setupListeners() {
        imgSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Settings feature coming soon", Toast.LENGTH_SHORT).show();
        });

        searchContainer.setOnClickListener(v -> {
            // Updated to use the Activity method instead of Fragment
            Intent intent = new Intent(ClientDashboardActivity.this, ClientRoomsListActivity.class);
            startActivity(intent);
        });

        // --- MERGE CONFLICT RESOLUTION: Using the "Stashed" changes ---

        cardRooms.setOnClickListener(v -> {
            Intent intent = new Intent(ClientDashboardActivity.this, ClientRoomsListActivity.class);
            startActivity(intent);
        });

        cardInstructors.setOnClickListener(v -> {
            Intent intent = new Intent(ClientDashboardActivity.this, ClientInstructorsListActivity.class);
            startActivity(intent);
        });

        // This checks permission, then calls openCamera()
        cardNav.setOnClickListener(v -> checkCameraPermissionAndOpen());

        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ClientDashboardActivity.this, ClientProfileActivity.class);
            startActivity(intent);
        });

        // --- NEW BOTTOM NAV LISTENERS ---
        // Home (already here, just refresh or do nothing)
        navHome.setOnClickListener(v -> {});

        // Navigation Button -> Open Nav Screen
        navNav.setOnClickListener(v -> {
            checkCameraPermissionAndOpen();
        });

        // Profile Button -> Open Profile
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ClientDashboardActivity.this, ClientProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    /* // OLD METHOD: If you decide to go back to Fragments instead of Activities, uncomment this.
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
    */

    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        // --- UPDATED LOGIC ---
        // Instead of opening the system camera, open your custom Navigation Activity
        Intent intent = new Intent(ClientDashboardActivity.this, ClientNavigationActivity.class);
        intent.putExtra("ROOM_NAME", "Navigation Mode"); // Optional title
        startActivity(intent);
        // Optional: Slide animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to use Navigation", Toast.LENGTH_SHORT).show();
            }
        }
    }
}