package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class RoomDetailsActivity extends AppCompatActivity {

    // 1. Declare UI Components (matching IDs from db_client_room_details.xml)
    private ImageView btnBack;
    private ImageView imgRoom; // Placeholder for image, likely loaded dynamically
    private TextView tvRoomName, tvRoomType; // Placeholder for room details
    private AppCompatButton btnStartNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link to the XML layout file
        setContentView(R.layout.db_client_room_details);

        // 2. Initialize Views
        initViews();

        // 3. Set up Click Listeners
        setupListeners();

        // Optional: Load dynamic data if passed from previous activity
        // loadRoomDetails();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        // Assuming there are TextViews for the Room Name and Type inside the CardView's LinearLayout
        // Since the XML does not provide IDs for the TextViews inside the CardView,
        // we'll rely on the main action button for functionality.
        btnStartNav = findViewById(R.id.btnStartNav);
    }

    private void setupListeners() {
        // --- Back Button Functionality ---
        btnBack.setOnClickListener(v -> {
            // Closes the current activity and returns to the previous one (ClientRoomsListActivity)
            finish();
        });

        // --- Start Navigation Button Logic ---
        btnStartNav.setOnClickListener(v -> {
            Toast.makeText(this, "Starting Navigation to Room 101...", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to the AR Navigation Screen (db_client_navscreen_armode.xml)
            // Intent intent = new Intent(RoomDetailsActivity.this, ARNavigationActivity.class);
            // startActivity(intent);
        });

        // Note: Bottom navigation bar items also need listeners if they are to be functional.
    }

    // private void loadRoomDetails() {
    //     // Example: Retrieving data passed from ClientRoomsListActivity
    //     Intent intent = getIntent();
    //     String roomName = intent.getStringExtra("ROOM_NAME");
    //     if (roomName != null && tvRoomName != null) {
    //         tvRoomName.setText(roomName);
    //     }
    // }
}