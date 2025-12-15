package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
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

        // Navigation
        LinearLayout bottomNav = findViewById(R.id.bottomNav);
        // Assuming children index 0=Maps, 1=Dashboard, 2=Updates based on XML structure
        // Or find by IDs if you added IDs to the linear layouts inside bottomNav

        btnBack.setOnClickListener(v -> finish());

        fabAdd.setOnClickListener(v -> showRoomDialog(null));
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

    private void showRoomDialog(Room room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(room == null ? "Add Room" : "Edit Room");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etName = new EditText(this);
        etName.setHint("Room Name (e.g. Room 101)");
        if (room != null) etName.setText(room.getRoomName());
        layout.addView(etName);

        final EditText etDesc = new EditText(this);
        etDesc.setHint("Description");
        if (room != null) etDesc.setText(room.getDescription());
        layout.addView(etDesc);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString();
            String desc = etDesc.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (room == null) {
                dbHelper.addRoom(name, desc);
            } else {
                dbHelper.updateRoom(room.getId(), name, desc);
            }
            loadRooms();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}