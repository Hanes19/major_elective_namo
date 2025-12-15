package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminRoomMapActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tabList, btnZoomIn, btnZoomOut;
    private TextView marker101, marker102, marker103, marker201, marker202;
    private LinearLayout navDashboard, navMap, navUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_room_map);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabList = findViewById(R.id.tabList);
        btnZoomIn = findViewById(R.id.btnZoomIn);
        btnZoomOut = findViewById(R.id.btnZoomOut);

        // Markers
        marker101 = findViewById(R.id.marker_101);
        marker102 = findViewById(R.id.marker_102);
        marker103 = findViewById(R.id.marker_103);
        marker201 = findViewById(R.id.marker_201);
        marker202 = findViewById(R.id.marker_202);

        // Bottom Navigation
        navDashboard = findViewById(R.id.navDashboard);
        navMap = findViewById(R.id.navMap);
        navUpdates = findViewById(R.id.navUpdates);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // --- Toggle Switch ---
        // Switch to Admin List View (Manage Rooms) instead of Student List
        tabList.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRoomMapActivity.this, AdminManageRoomsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish(); // Optional: finish map so back button logic is cleaner
        });

        // --- Marker Logic ---
        View.OnClickListener markerListener = v -> {
            TextView tv = (TextView) v;
            String roomNumber = tv.getText().toString();

            // Open Room Details (Same function as student)
            Intent intent = new Intent(AdminRoomMapActivity.this, RoomDetailsActivity.class);
            intent.putExtra("ROOM_NAME", "Room " + roomNumber);
            startActivity(intent);
        };

        if(marker101 != null) marker101.setOnClickListener(markerListener);
        if(marker102 != null) marker102.setOnClickListener(markerListener);
        if(marker103 != null) marker103.setOnClickListener(markerListener);
        if(marker201 != null) marker201.setOnClickListener(markerListener);
        if(marker202 != null) marker202.setOnClickListener(markerListener);

        // Zoom Controls
        btnZoomIn.setOnClickListener(v -> Toast.makeText(this, "Zoom In", Toast.LENGTH_SHORT).show());
        btnZoomOut.setOnClickListener(v -> Toast.makeText(this, "Zoom Out", Toast.LENGTH_SHORT).show());

        // --- Bottom Navigation ---
        navDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRoomMapActivity.this, AdminDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        navMap.setOnClickListener(v -> {
            // Already on Map
            Toast.makeText(this, "You are already viewing the Map", Toast.LENGTH_SHORT).show();
        });

        navUpdates.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRoomMapActivity.this, AdminReportsActivity.class);
            startActivity(intent);
            finish();
        });
    }
}