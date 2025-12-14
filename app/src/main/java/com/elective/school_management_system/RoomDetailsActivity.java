package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
        setContentView(R.layout.db_student_room_details);

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
        if (getIntent().hasExtra("ROOM_ID")) {
            roomId = getIntent().getIntExtra("ROOM_ID", -1);
            currentRoom = dbHelper.getRoomById(roomId);
            if (currentRoom != null) {
                tvTitle.setText(currentRoom.getRoomName());
                tvDesc.setText(currentRoom.getDescription());
            }
        } else if (getIntent().hasExtra("ROOM_NAME")) {
            String roomName = getIntent().getStringExtra("ROOM_NAME");
            tvTitle.setText(roomName);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // --- UPDATED LOGIC ---
        btnStartNav.setOnClickListener(v -> {
            String roomName = (tvTitle.getText() != null) ? tvTitle.getText().toString() : "Room";

            // Launch the custom ClientNavigationActivity
            Intent intent = new Intent(RoomDetailsActivity.this, StudentNavigationActivity.class);
            intent.putExtra("ROOM_NAME", roomName);
            startActivity(intent);
            // Optional animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}