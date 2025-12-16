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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class StudentRoomMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton btnBack;
    private TextView tabList;
    private Button btnNavigateSchool; // New button
    private GoogleMap mMap;

    // Default School Coordinates (Replace with actual)
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
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Switch back to List View
        tabList.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRoomMapActivity.this, StudentRoomsListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // ACTION: Open Google Maps App for Navigation
        btnNavigateSchool.setOnClickListener(v -> {
            // "google.navigation:q=latitude,longitude"
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

    private void setupMap() {
        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 1. Enable User Location
        enableUserLocation();

        // 2. Add Room Markers
        List<Room> rooms = getMockRooms();

        for (Room room : rooms) {
            LatLng location = new LatLng(room.getLatitude(), room.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(room.getRoomName())
                    .snippet(room.getDescription()));
        }

        // 3. Move Camera to default (School Center)
        LatLng center = new LatLng(SCHOOL_LAT, SCHOOL_LNG);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 18f));

        // 4. Handle Marker Clicks
        mMap.setOnInfoWindowClickListener(marker -> {
            Intent intent = new Intent(StudentRoomMapActivity.this, RoomDetailsActivity.class);
            intent.putExtra("ROOM_NAME", marker.getTitle());
            startActivity(intent);
        });
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private List<Room> getMockRooms() {
        List<Room> rooms = new ArrayList<>();
        // Mock data now works with the updated Room constructor
        rooms.add(new Room(1, "Room 101", "Math Lab", "room_101", 14.5995, 120.9842));
        rooms.add(new Room(2, "Room 102", "Science Lab", "room_102", 14.5996, 120.9843));
        return rooms;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            } else {
                Toast.makeText(this, "Location permission needed to show your position", Toast.LENGTH_SHORT).show();
            }
        }
    }
}