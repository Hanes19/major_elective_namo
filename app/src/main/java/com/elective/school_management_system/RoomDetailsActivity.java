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

        tvTitle = findViewById(R.id.tvRoomTitle);
        tvDesc = findViewById(R.id.tvRoomDesc);
    }

    private void loadRoomDetails() {
        // Handle loading by ID (from Database)
        if (getIntent().hasExtra("ROOM_ID")) {
            roomId = getIntent().getIntExtra("ROOM_ID", -1);
            currentRoom = dbHelper.getRoomById(roomId);

            if (currentRoom != null) {
                tvTitle.setText(currentRoom.getRoomName());
                tvDesc.setText(currentRoom.getDescription());
            }
        }
        // Handle loading by Name (from Map shortcuts)
        else if (getIntent().hasExtra("ROOM_NAME")) {
            String roomName = getIntent().getStringExtra("ROOM_NAME");
            tvTitle.setText(roomName);
            // Optional: You could fetch the full object by name if you added a helper method
            // currentRoom = dbHelper.getRoomByName(roomName);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // FIX: Replaced Unity Launcher with Camera Intent
        btnStartNav.setOnClickListener(v -> {
            String roomName = (tvTitle.getText() != null) ? tvTitle.getText().toString() : "Room";
            Toast.makeText(this, "Opening Camera for: " + roomName, Toast.LENGTH_SHORT).show();

            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show();
            }
        });
    }
}