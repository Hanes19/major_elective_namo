package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ClientRoomMapActivity extends AppCompatActivity {

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
        // Close activity (Go back)
        btnBack.setOnClickListener(v -> finish());

        // Switch back to List View
        tabList.setOnClickListener(v -> {
            // Intent intent = new Intent(ClientRoomMapActivity.this, ClientRoomsListActivity.class);
            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // startActivity(intent);
            finish(); // Since we usually come from List, finish() acts as "Back to List"
        });

        // --- MAP MARKER CLICKS ---
        View.OnClickListener markerListener = v -> {
            TextView tv = (TextView) v;
            String roomNumber = tv.getText().toString();
            String roomName = "Room " + roomNumber;

            // Navigate to Details Page
            Intent intent = new Intent(ClientRoomMapActivity.this, RoomDetailsActivity.class);
            intent.putExtra("ROOM_NAME", roomName);
            startActivity(intent);
        };

        marker101.setOnClickListener(markerListener);
        marker102.setOnClickListener(markerListener);
        marker103.setOnClickListener(markerListener);
        marker201.setOnClickListener(markerListener);
        marker202.setOnClickListener(markerListener);

        // Zoom Controls (Mock)
        btnZoomIn.setOnClickListener(v -> Toast.makeText(this, "Zooming In...", Toast.LENGTH_SHORT).show());
        btnZoomOut.setOnClickListener(v -> Toast.makeText(this, "Zooming Out...", Toast.LENGTH_SHORT).show());
    }
}