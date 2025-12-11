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
    private TextView tvTitle, tvDesc; // These will match tvRoomTitle and tvRoomDesc

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
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnStartNav.setOnClickListener(v -> {
            if (currentRoom != null) {
                Toast.makeText(this, "Starting AR for: " + currentRoom.getRoomName(), Toast.LENGTH_SHORT).show();
                try {
                    Intent intent = new Intent(RoomDetailsActivity.this, com.unity3d.player.UnityPlayerActivity.class);
                    intent.putExtra("destination", currentRoom.getArDestinationId());
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    Toast.makeText(this, "AR Module not installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}