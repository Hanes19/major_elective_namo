package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ClientRoomsListActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout tabList, searchContainer;
    private TextView tabMap;

    // List Items
    private LinearLayout itemRoom102, itemRoom103, itemRoom104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_client_rooms_list);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabList = findViewById(R.id.tabList);
        tabMap = findViewById(R.id.tabMap);
        searchContainer = findViewById(R.id.searchContainer);

        // Find the items using the IDs we just added to the XML
        itemRoom102 = findViewById(R.id.item_room_102);
        itemRoom103 = findViewById(R.id.item_room_103);
        itemRoom104 = findViewById(R.id.item_room_104);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Tab Logic (Visual Placeholder for now)
        tabMap.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Map View...", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(ClientRoomsListActivity.this, ClientRoomMapActivity.class);
            // startActivity(intent);
        });

        searchContainer.setOnClickListener(v -> {
            Toast.makeText(this, "Search feature coming soon", Toast.LENGTH_SHORT).show();
        });

        // --- List Item Clicks ---
        // We pass specific Room Names to the Details Activity
        itemRoom102.setOnClickListener(v -> navigateToRoomDetails("Room 102", "Computer Lab 2"));
        itemRoom103.setOnClickListener(v -> navigateToRoomDetails("Room 103", "Lecture Hall A"));
        itemRoom104.setOnClickListener(v -> navigateToRoomDetails("Room 104", "Science Lab"));
    }

    private void navigateToRoomDetails(String roomName, String roomDesc) {
        Intent intent = new Intent(ClientRoomsListActivity.this, RoomDetailsActivity.class);
        intent.putExtra("ROOM_NAME", roomName);
        intent.putExtra("ROOM_DESC", roomDesc);
        startActivity(intent);
    }
}