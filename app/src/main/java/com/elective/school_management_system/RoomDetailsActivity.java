package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class RoomDetailsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private AppCompatButton btnStartNav;

    // Note: If you add IDs to the TextViews in XML, you can change text dynamically
    // private TextView tvRoomName, tvRoomType;

    private String currentRoomName = "Room 101"; // Default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_client_room_details);

        initViews();
        loadRoomDetails();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnStartNav = findViewById(R.id.btnStartNav);
    }

    private void loadRoomDetails() {
        // Get the data passed from the previous list screen
        if (getIntent().hasExtra("ROOM_NAME")) {
            currentRoomName = getIntent().getStringExtra("ROOM_NAME");
            // String roomDesc = getIntent().getStringExtra("ROOM_DESC");
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // --- START AR NAVIGATION ---
        btnStartNav.setOnClickListener(v -> {
            Toast.makeText(this, "Starting AR for: " + currentRoomName, Toast.LENGTH_SHORT).show();

            // 1. Create intent for Unity
            Intent intent = new Intent(RoomDetailsActivity.this, com.unity3d.player.UnityPlayerActivity.class);

            // 2. Pass the room name so Unity knows where to point the arrow
            intent.putExtra("destination", currentRoomName);

            // 3. Launch Unity
            startActivity(intent);
        });
    }
}