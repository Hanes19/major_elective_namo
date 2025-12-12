package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore; // Import added for Camera
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class RoomDetailsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private AppCompatButton btnStartNav;
    private TextView tvTitle, tvDesc;

    private DatabaseHelper dbHelper;
    private Room currentRoom;
    private int roomId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_client_room_details);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadRoomDetails();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnStartNav = findViewById(R.id.btnStartNav);

        // Connect to the new IDs in the XML
        tvTitle = findViewById(R.id.tvRoomTitle);
        tvDesc = findViewById(R.id.tvRoomDesc);
    }

    private void loadRoomDetails() {
        if (getIntent().hasExtra("ROOM_ID")) {
            roomId = getIntent().getIntExtra("ROOM_ID", -1);
            currentRoom = dbHelper.getRoomById(roomId);

            if (currentRoom != null) {
                tvTitle.setText(currentRoom.getRoomName());
                tvDesc.setText(currentRoom.getDescription());
            }
        }
        // Fallback for intent from Map which might send "ROOM_NAME" string instead of ID
        else if (getIntent().hasExtra("ROOM_NAME")) {
            String roomName = getIntent().getStringExtra("ROOM_NAME");
            tvTitle.setText(roomName);
            // Ideally fetch details by name from DB here if needed
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnStartNav.setOnClickListener(v -> {
            if (currentRoom != null || tvTitle.getText().length() > 0) {
                String roomName = (currentRoom != null) ? currentRoom.getRoomName() : tvTitle.getText().toString();

                // TEMPORARY: Replaced Unity AR with Camera
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                    Toast.makeText(RoomDetailsActivity.this, "Opening Camera for: " + roomName, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RoomDetailsActivity.this, "Unable to open camera", Toast.LENGTH_SHORT).show();
                }

                /* // ORIGINAL CODE (Commented out to fix "Cannot resolve symbol unity3d")
                try {
                    Intent intent = new Intent(RoomDetailsActivity.this, com.unity3d.player.UnityPlayerActivity.class);
                    intent.putExtra("destination", currentRoom.getArDestinationId());
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    Toast.makeText(this, "AR Module not installed.", Toast.LENGTH_SHORT).show();
                }
                */
            }
        });
    }
}