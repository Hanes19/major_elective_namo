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
    private TextView tvTitle, tvDesc;

    private DatabaseHelper dbHelper;
    private Room currentRoom;
    private int roomId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_client_room_details);

        dbHelper = new DatabaseHelper(this);

        // Find Views (Ensure your XML has IDs: tvRoomTitle and tvRoomDesc)
        // I'm assuming you will add android:id="@+id/tvRoomTitle" to the Room Name TextView
        // and android:id="@+id/tvRoomDesc" to the Description TextView in your XML.
        btnBack = findViewById(R.id.btnBack);
        btnStartNav = findViewById(R.id.btnStartNav);

        // You MUST update your XML to include these IDs, or find them by hierarchy
        // Ideally, update db_client_room_details.xml TextViews with IDs:
        tvTitle = findViewById(R.id.tvRoomTitle); // Placeholder ID
        tvDesc = findViewById(R.id.tvRoomDesc);   // Placeholder ID

        loadRoomDetails();
        setupListeners();
    }

    private void loadRoomDetails() {
        if (getIntent().hasExtra("ROOM_ID")) {
            roomId = getIntent().getIntExtra("ROOM_ID", -1);
            currentRoom = dbHelper.getRoomById(roomId);

            if (currentRoom != null) {
                // If you updated XML with IDs, uncomment these:
                // tvTitle.setText(currentRoom.getRoomName());
                // tvDesc.setText(currentRoom.getDescription());

                // Temporary fix if you haven't updated XML IDs yet:
                TextView titleInLayout = findViewById(R.id.cardView).findViewById(android.R.id.text1); // Requires correct ID
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
                    // Pass the technical ID (e.g., "room_101") that Unity understands
                    intent.putExtra("destination", currentRoom.getArDestinationId());
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    Toast.makeText(this, "AR Module not installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}