package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminManageRoomsActivity extends AppCompatActivity {

    private ImageButton btnBack, fabAdd;
    private RecyclerView recyclerView;
    private AdminRoomAdapter adapter;
    private DatabaseHelper dbHelper;
    private LinearLayout navMaps, navDashboard, navUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_manage_rooms);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadRooms();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        fabAdd = findViewById(R.id.fabAdd);
        recyclerView = findViewById(R.id.recyclerViewRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // These IDs must match the XML provided above
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        btnBack.setOnClickListener(v -> finish());
        fabAdd.setOnClickListener(v -> showRoomDialog(null));

        // Navigation Logic
        if (navMaps != null) {
            navMaps.setOnClickListener(v -> {
                Intent intent = new Intent(AdminManageRoomsActivity.this, AdminRoomMapActivity.class);
                startActivity(intent);
            });
        }

        if (navDashboard != null) {
            navDashboard.setOnClickListener(v -> {
                Intent intent = new Intent(AdminManageRoomsActivity.this, AdminDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        if (navUpdates != null) {
            navUpdates.setOnClickListener(v -> {
                Intent intent = new Intent(AdminManageRoomsActivity.this, AdminReportsActivity.class);
                startActivity(intent);
            });
        }
    }

    private void loadRooms() {
        List<Room> list = dbHelper.getAllRooms();
        adapter = new AdminRoomAdapter(this, list, new AdminRoomAdapter.OnRoomActionListener() {
            @Override
            public void onEdit(Room room) {
                showRoomDialog(room);
            }

            @Override
            public void onDelete(Room room) {
                new AlertDialog.Builder(AdminManageRoomsActivity.this)
                        .setTitle("Delete Room")
                        .setMessage("Are you sure you want to delete " + room.getRoomName() + "?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            dbHelper.deleteRoom(room.getId());
                            loadRooms();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    // Handles Adding and Editing Rooms with Lat/Long for Maps
    private void showRoomDialog(Room room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(room == null ? "Add Room" : "Edit Room");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etName = new EditText(this);
        etName.setHint("Room Name (e.g. 101)");
        if (room != null) etName.setText(room.getRoomName());
        layout.addView(etName);

        final EditText etDesc = new EditText(this);
        etDesc.setHint("Description (e.g. Ground Floor)");
        if (room != null) etDesc.setText(room.getDescription());
        layout.addView(etDesc);

        // Added Latitude Field
        final EditText etLat = new EditText(this);
        etLat.setHint("Latitude (e.g. 7.9230)");
        etLat.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (room != null) etLat.setText(String.valueOf(room.getLatitude()));
        layout.addView(etLat);

        // Added Longitude Field
        final EditText etLng = new EditText(this);
        etLng.setHint("Longitude (e.g. 125.0953)");
        etLng.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (room != null) etLng.setText(String.valueOf(room.getLongitude()));
        layout.addView(etLng);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            String latStr = etLat.getText().toString().trim();
            String lngStr = etLng.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Room name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Default to 0.0 if empty
            double lat = latStr.isEmpty() ? 0.0 : Double.parseDouble(latStr);
            double lng = lngStr.isEmpty() ? 0.0 : Double.parseDouble(lngStr);

            if (room == null) {
                // IMPORTANT: Ensure your dbHelper.addRoom supports lat/long arguments!
                // If not, you must update DatabaseHelper.java
                // boolean success = dbHelper.addRoom(name, desc, lat, lng);
                boolean success = dbHelper.addRoom(name, desc); // Fallback if DB not updated yet
                if(success) Toast.makeText(this, "Room Added", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "Error Adding Room", Toast.LENGTH_SHORT).show();
            } else {
                // boolean success = dbHelper.updateRoom(room.getId(), name, desc, lat, lng);
                boolean success = dbHelper.updateRoom(room.getId(), name, desc);
                if(success) Toast.makeText(this, "Room Updated", Toast.LENGTH_SHORT).show();
            }
            loadRooms();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}