package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentRoomMapActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tabList, btnZoomIn, btnZoomOut;
    private TextView marker101, marker102, marker103, marker201, marker202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_client_room_map);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabList = findViewById(R.id.tabList);
        btnZoomIn = findViewById(R.id.btnZoomIn);
        btnZoomOut = findViewById(R.id.btnZoomOut);

        marker101 = findViewById(R.id.marker_101);
        marker102 = findViewById(R.id.marker_102);
        marker103 = findViewById(R.id.marker_103);
        marker201 = findViewById(R.id.marker_201);
        marker202 = findViewById(R.id.marker_202);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Switch back to List View
        tabList.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRoomMapActivity.this, StudentRoomsListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // Marker Logic
        View.OnClickListener markerListener = v -> {
            TextView tv = (TextView) v;
            String roomNumber = tv.getText().toString(); // e.g., "101"

            Intent intent = new Intent(StudentRoomMapActivity.this, RoomDetailsActivity.class);
            intent.putExtra("ROOM_NAME", "Room " + roomNumber);
            startActivity(intent);
        };

        if(marker101 != null) marker101.setOnClickListener(markerListener);
        if(marker102 != null) marker102.setOnClickListener(markerListener);
        if(marker103 != null) marker103.setOnClickListener(markerListener);
        if(marker201 != null) marker201.setOnClickListener(markerListener);
        if(marker202 != null) marker202.setOnClickListener(markerListener);

        btnZoomIn.setOnClickListener(v -> Toast.makeText(this, "Zoom In", Toast.LENGTH_SHORT).show());
        btnZoomOut.setOnClickListener(v -> Toast.makeText(this, "Zoom Out", Toast.LENGTH_SHORT).show());
    }
}