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

        itemRoom102 = findViewById(R.id.item_room_102);
        itemRoom103 = findViewById(R.id.item_room_103);
        itemRoom104 = findViewById(R.id.item_room_104);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Switch to Map View
        tabMap.setOnClickListener(v -> {
            Intent intent = new Intent(ClientRoomsListActivity.this, ClientRoomMapActivity.class);
            // Don't finish() here if you want to keep the back stack state
            startActivity(intent);
        });

        searchContainer.setOnClickListener(v ->
                Toast.makeText(this, "Search coming soon", Toast.LENGTH_SHORT).show());

        // Navigation to Details
        if(itemRoom102 != null) itemRoom102.setOnClickListener(v -> navigateToRoomDetails("Room 102"));
        if(itemRoom103 != null) itemRoom103.setOnClickListener(v -> navigateToRoomDetails("Room 103"));
        if(itemRoom104 != null) itemRoom104.setOnClickListener(v -> navigateToRoomDetails("Room 104"));
    }

    private void navigateToRoomDetails(String roomName) {
        Intent intent = new Intent(ClientRoomsListActivity.this, RoomDetailsActivity.class);
        intent.putExtra("ROOM_NAME", roomName);
        startActivity(intent);
    }
}