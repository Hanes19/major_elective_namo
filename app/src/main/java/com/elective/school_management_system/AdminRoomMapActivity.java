package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class AdminRoomMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton btnBack;
    private TextView tabList;
    private LinearLayout navDashboard, navMap, navUpdates;
    private GoogleMap mMap;

    // TODO: Update these coordinates to your school's real location
    private static final double SCHOOL_LAT = 14.5995;
    private static final double SCHOOL_LNG = 120.9842;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_room_map);

        initViews();
        setupListeners();
        setupMap();
    }

    private void initViews() {
        // We only initialize views that actually exist in the new layout
        btnBack = findViewById(R.id.btnBack);
        tabList = findViewById(R.id.tabList);

        // Bottom Navigation
        navDashboard = findViewById(R.id.navDashboard);
        navMap = findViewById(R.id.navMap);
        navUpdates = findViewById(R.id.navUpdates);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Switch to List View
        tabList.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRoomMapActivity.this, AdminManageRoomsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Bottom Navigation Logic
        navDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRoomMapActivity.this, AdminDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        navMap.setOnClickListener(v -> {
            Toast.makeText(this, "You are already viewing the Map", Toast.LENGTH_SHORT).show();
        });

        navUpdates.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRoomMapActivity.this, AdminReportsActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 1. Move Camera to School
        LatLng schoolLocation = new LatLng(SCHOOL_LAT, SCHOOL_LNG);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(schoolLocation, 18f));

        // 2. Add Markers Programmatically (instead of using TextViews)
        addRoomMarkers();

        // 3. Handle Marker Clicks
        mMap.setOnInfoWindowClickListener(marker -> {
            String roomTitle = marker.getTitle(); // e.g., "Room 101"
            Intent intent = new Intent(AdminRoomMapActivity.this, RoomDetailsActivity.class);
            intent.putExtra("ROOM_NAME", roomTitle);
            startActivity(intent);
        });
    }

    private void addRoomMarkers() {
        // In a real app, you would fetch these from your database
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(101, "101", "Ground Floor", "ar_101", SCHOOL_LAT + 0.0001, SCHOOL_LNG + 0.0001));
        rooms.add(new Room(102, "102", "Ground Floor", "ar_102", SCHOOL_LAT - 0.0001, SCHOOL_LNG - 0.0001));
        rooms.add(new Room(201, "201", "Second Floor", "ar_201", SCHOOL_LAT + 0.0002, SCHOOL_LNG - 0.0001));
        rooms.add(new Room(202, "202", "Second Floor", "ar_202", SCHOOL_LAT - 0.0002, SCHOOL_LNG + 0.0001));

        for (Room room : rooms) {
            LatLng pos = new LatLng(room.getLatitude(), room.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title("Room " + room.getRoomName())
                    .snippet(room.getDescription())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        }
    }
}