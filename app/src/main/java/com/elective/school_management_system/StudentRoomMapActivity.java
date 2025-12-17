package com.elective.school_management_system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
    // REMOVED: Bottom Navigation variables (navHome, navProfile) as they are not in the XML

    private GoogleMap mMap;
    private LatLng selectedDestination;
    private String selectedBuildingName;

    // School Coordinates
    private static final double SCHOOL_LAT = 7.9230;
    private static final double SCHOOL_LNG = 125.0953;

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

        // REMOVED: finding navHome and navProfile
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        tabList.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRoomMapActivity.this, StudentRoomsListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnNavigateSchool.setOnClickListener(v -> {
            if (selectedDestination == null) {
                selectedDestination = new LatLng(SCHOOL_LAT, SCHOOL_LNG);
            }

            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + selectedDestination.latitude + "," + selectedDestination.longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
            }
        });

        // REMOVED: navHome and navProfile listeners to prevent Crash
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

        LatLng schoolLocation = new LatLng(SCHOOL_LAT, SCHOOL_LNG);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(schoolLocation, 17.5f));

        enableUserLocation();
        addBuildingMarkers();

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
        buildings.add(new Room(1, "TS Building", "Main Campus", "", SCHOOL_LAT, SCHOOL_LNG));
        buildings.add(new Room(2, "Annex A", "Rooms 101-105", "", SCHOOL_LAT + 0.0001, SCHOOL_LNG + 0.0001));
        buildings.add(new Room(3, "Library", "Study Area", "", SCHOOL_LAT - 0.0001, SCHOOL_LNG - 0.0001));

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