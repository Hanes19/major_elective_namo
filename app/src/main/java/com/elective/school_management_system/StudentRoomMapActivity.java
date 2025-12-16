package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class StudentRoomMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton btnBack;
    private TextView tabList;
    private Button btnNavigateSchool;
    private LinearLayout navHome, navNavigation, navProfile;

    private GoogleMap mMap;

    private LatLng selectedDestination;
    private String selectedBuildingName;

    // Campus Coordinates
    private static final double SCHOOL_LAT = 14.5995;
    private static final double SCHOOL_LNG = 120.9842;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_room_map);

        initViews();
        setupListeners();
        setupMap();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabList = findViewById(R.id.tabList);
        btnNavigateSchool = findViewById(R.id.btnNavigateSchool);

        // Bottom Navigation
        navHome = findViewById(R.id.navHome);
        navNavigation = findViewById(R.id.navNavigation);
        navProfile = findViewById(R.id.navProfile);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Switch to List View
        tabList.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRoomMapActivity.this, StudentRoomsListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // "Get Directions" - Opens External Google Maps
        btnNavigateSchool.setOnClickListener(v -> {
            if (selectedDestination == null) {
                // Default to main school coordinates if no building selected
                selectedDestination = new LatLng(SCHOOL_LAT, SCHOOL_LNG);
            }

            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + selectedDestination.latitude + "," + selectedDestination.longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Google Maps app is not installed.", Toast.LENGTH_SHORT).show();
            }
        });

        // --- Bottom Navigation Logic ---
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRoomMapActivity.this, StudentDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        navNavigation.setOnClickListener(v -> {
            Toast.makeText(this, "You are already on the Map.", Toast.LENGTH_SHORT).show();
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRoomMapActivity.this, StudentProfileActivity.class);
            startActivity(intent);
        });
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 1. Move Camera to School
        LatLng schoolLocation = new LatLng(SCHOOL_LAT, SCHOOL_LNG);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(schoolLocation, 17.5f));

        // 2. Enable User Location
        enableUserLocation();

        // 3. Add Building Markers
        addBuildingMarkers();

        // 4. Handle Marker Clicks
        mMap.setOnMarkerClickListener(marker -> {
            selectedDestination = marker.getPosition();
            selectedBuildingName = marker.getTitle();
            marker.showInfoWindow();
            btnNavigateSchool.setText("Get Directions to " + selectedBuildingName);
            return false;
        });
    }

    private void addBuildingMarkers() {
        List<Room> buildings = new ArrayList<>();
        // Mock Data
        buildings.add(new Room(1, "Main Building", "Admin & Registrar", "", SCHOOL_LAT, SCHOOL_LNG));
        buildings.add(new Room(2, "Engineering Bldg", "Rooms 101-105", "", SCHOOL_LAT + 0.0002, SCHOOL_LNG + 0.0002));
        buildings.add(new Room(3, "Library", "Study Area", "", SCHOOL_LAT - 0.0002, SCHOOL_LNG - 0.0002));

        for (Room b : buildings) {
            LatLng pos = new LatLng(b.getLatitude(), b.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(b.getRoomName())
                    .snippet(b.getDescription())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        }
    }
}