package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ClientDashboardActivity extends AppCompatActivity {

    private ImageView imgSettings;
    private LinearLayout searchContainer;
    private LinearLayout cardRooms, cardInstructors, cardNav, cardProfile;
    private View item1, item2, item3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_client_first_page);

        initViews();
        setupListeners();
    }

    private void initViews() {
        imgSettings = findViewById(R.id.img_settings);
        searchContainer = findViewById(R.id.search_container);
        cardRooms = findViewById(R.id.card_rooms);
        cardInstructors = findViewById(R.id.card_instructors);
        cardNav = findViewById(R.id.card_nav);
        cardProfile = findViewById(R.id.card_profile);
        item1 = findViewById(R.id.item_1);
        item2 = findViewById(R.id.item_2);
        item3 = findViewById(R.id.item_3);
    }

    private void setupListeners() {
        imgSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Settings feature coming soon", Toast.LENGTH_SHORT).show();
        });

        searchContainer.setOnClickListener(v -> {
            Intent intent = new Intent(ClientDashboardActivity.this, ClientRoomsListActivity.class);
            startActivity(intent);
        });

        // 1. Navigate to Rooms List
        cardRooms.setOnClickListener(v -> {
            Intent intent = new Intent(ClientDashboardActivity.this, ClientRoomsListActivity.class);
            startActivity(intent);
        });

        // 2. Navigate to Instructors (FIXED: Links to Client List)
        cardInstructors.setOnClickListener(v -> {
            Intent intent = new Intent(ClientDashboardActivity.this, ClientInstructorsListActivity.class);
            startActivity(intent);
        });

        // 3. Navigate to Camera
        cardNav.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Navigate to Profile
        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ClientDashboardActivity.this, ClientProfileActivity.class);
            startActivity(intent);
        });

        // Shortcuts
        item1.setOnClickListener(v -> {
            Intent intent = new Intent(ClientDashboardActivity.this, RoomDetailsActivity.class);
            intent.putExtra("ROOM_NAME", "Computer Lab 3");
            startActivity(intent);
        });

        item2.setOnClickListener(v -> {
            // Updated to link to Instructors list
            Intent intent = new Intent(ClientDashboardActivity.this, ClientInstructorsListActivity.class);
            startActivity(intent);
        });

        item3.setOnClickListener(v -> {
            Intent intent = new Intent(ClientDashboardActivity.this, RoomDetailsActivity.class);
            intent.putExtra("ROOM_NAME", "Room 103");
            startActivity(intent);
        });
    }
}