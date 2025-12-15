package com.elective.school_management_system;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TeacherAssignedRoomsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etSearch;
    private RecyclerView rvAssignedRooms;
    // private RoomAdapter adapter; // Reuse your existing RoomAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_assigned_rooms);

        initViews();
        setupRecyclerView();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etSearch = findViewById(R.id.etSearch);
        rvAssignedRooms = findViewById(R.id.rvAssignedRooms);

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        rvAssignedRooms.setLayoutManager(new LinearLayoutManager(this));
        // adapter = new RoomAdapter(this, new ArrayList<>()); // Pass actual data here
        // rvAssignedRooms.setAdapter(adapter);
    }
}