package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Note: You will need to register this activity in your AndroidManifest.xml
public class ClientDashboardActivity extends AppCompatActivity {

    // 1. Declare UI Components (matching IDs from db_client_first_page.xml)
    private ImageView imgSettings;
    private LinearLayout searchContainer;
    private LinearLayout cardRooms, cardInstructors, cardNav, cardProfile;
    private View item1, item2, item3; // The suggestion items in the ScrollView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link to the XML layout file
        setContentView(R.layout.db_client_first_page);

        // 2. Initialize Views
        initViews();

        // 3. Set up Click Listeners
        setupListeners();
    }

    private void initViews() {
        // Header
        imgSettings = findViewById(R.id.img_settings);

        // Main Sections
        searchContainer = findViewById(R.id.search_container);
        cardRooms = findViewById(R.id.card_rooms);
        cardInstructors = findViewById(R.id.card_instructors);
        cardNav = findViewById(R.id.card_nav);
        cardProfile = findViewById(R.id.card_profile);

        // Suggestions List Items (using their container IDs)
        item1 = findViewById(R.id.item_1);
        item2 = findViewById(R.id.item_2);
        item3 = findViewById(R.id.item_3);

        // Note: The bottom navigation bar is present in the XML but usually managed
        // via fragments or dedicated handling logic, which is beyond this boilerplate setup.
    }

    private void setupListeners() {
        // Handle Clicks for Header and Search
        imgSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Settings Activity
        });

        searchContainer.setOnClickListener(v -> {
            Toast.makeText(this, "Open Search Screen", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to a dedicated Search/Map Screen
        });

        cardRooms.setOnClickListener(v -> {
            // Navigate to the Rooms List Activity (ClientRoomsListActivity)
            Intent intent = new Intent(ClientDashboardActivity.this, ClientRoomsListActivity.class);
            startActivity(intent);
        });

        cardInstructors.setOnClickListener(v -> {
            Toast.makeText(this, "Instructors clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Instructors List Activity
        });

        cardNav.setOnClickListener(v -> {
            // Start the Unity AR Activity
            Intent intent = new Intent(ClientDashboardActivity.this, com.unity3d.player.UnityPlayerActivity.class);

            // Optional: Pass the room name to Unity (so your script knows where to point)
            intent.putExtra("destination", "Room 101");

            startActivity(intent);
        });

        cardProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Profile Activity (p_profile_screen.xml)
        });

        // Handle Clicks for Suggested Items
        item1.setOnClickListener(v -> {
            Toast.makeText(this, "Computer Lab 3 Details clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Room Details (db_client_room_details.xml)
        });

        item2.setOnClickListener(v -> {
            Toast.makeText(this, "Professor Marcelo Dupalco Details clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Instructor Profile Details
        });

        item3.setOnClickListener(v -> {
            Toast.makeText(this, "Room 103 Details clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Room Details (db_client_room_details.xml)
        });
    }
}