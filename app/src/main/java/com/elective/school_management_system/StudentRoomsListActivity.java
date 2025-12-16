package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentRoomsListActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout navMap, navDashboard, navUpdates;
    private EditText etSearch;
    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private DatabaseHelper dbHelper;
    private boolean isGuest;
    private boolean showDescOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_rooms_list);

        isGuest = getIntent().getBooleanExtra("IS_GUEST", false);
        showDescOnly = getIntent().getBooleanExtra("SHOW_DESC_ONLY", false);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupRecyclerView();
        setupListeners();

        if (isGuest) {
            setupGuestMode();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        navMap = findViewById(R.id.navMap);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerViewRooms);
    }

    private void setupGuestMode() {
        // Hide student-specific navigation
        if (navMap != null) navMap.setVisibility(View.GONE);
        if (navDashboard != null) navDashboard.setVisibility(View.GONE);
        if (navUpdates != null) navUpdates.setVisibility(View.GONE);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Room> allRooms = dbHelper.getAllRooms();
        // Displays all rooms by default (acting as suggestions)
        // Pass the showDescOnly flag to the adapter
        adapter = new RoomAdapter(this, allRooms, showDescOnly);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Navigation (Only active if not guest, or if visible)
        if (!isGuest) {
            navMap.setOnClickListener(v -> {
                Intent intent = new Intent(StudentRoomsListActivity.this, StudentRoomMapActivity.class);
                startActivity(intent);
            });

            navDashboard.setOnClickListener(v -> {
                Intent intent = new Intent(StudentRoomsListActivity.this, StudentDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            });

            navUpdates.setOnClickListener(v ->
                    Toast.makeText(this, "Updates coming soon...", Toast.LENGTH_SHORT).show()
            );
        }

        // Room Suggestion / Search Logic
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    // Filters the list in real-time providing suggestions based on input
                    List<Room> filteredList = dbHelper.searchRooms(s.toString());
                    adapter.updateList(filteredList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}