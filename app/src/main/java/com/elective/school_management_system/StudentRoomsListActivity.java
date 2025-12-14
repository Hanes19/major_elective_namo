package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentRoomsListActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tabMap;
    private EditText etSearch;
    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_student_rooms_list);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabMap = findViewById(R.id.tabMap);

        // These IDs must match the XML provided above
        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerViewRooms);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Room> allRooms = dbHelper.getAllRooms();
        adapter = new RoomAdapter(this, allRooms);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        tabMap.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRoomsListActivity.this, StudentRoomMapActivity.class);
            startActivity(intent);
        });

        // Search Logic
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    List<Room> filteredList = dbHelper.searchRooms(s.toString());
                    adapter.updateList(filteredList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}