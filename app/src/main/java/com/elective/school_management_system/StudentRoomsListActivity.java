package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView; // Changed from LinearLayout
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentRoomsListActivity extends AppCompatActivity {

    // Views
    private View btnBack; // Changed to View to match ImageButton or generic View
    private TextView tabMap; // CHANGED: Matches the 'tabMap' ID in your XML toggle
    private EditText etSearch;
    private RecyclerView recyclerView;

    // Logic
    private RoomAdapter adapter;
    private DatabaseHelper dbHelper;
    private boolean isGuest;
    private boolean showDescOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure this matches your XML file name (e.g., s_navigation_list or s_rooms_list)
        setContentView(R.layout.s_navigation_list);

        // Get Intent Extras
        isGuest = getIntent().getBooleanExtra("IS_GUEST", false);
        showDescOnly = getIntent().getBooleanExtra("SHOW_DESC_ONLY", false);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        // FIX: Use tabMap (TextView) instead of navMap
        tabMap = findViewById(R.id.tabMap);

        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerViewRooms);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Room> allRooms = dbHelper.getAllRooms();
        adapter = new RoomAdapter(this, allRooms, showDescOnly);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        // Back Button
        btnBack.setOnClickListener(v -> finish());

        // Toggle Switch: Go to Map
        if (tabMap != null) {
            tabMap.setOnClickListener(v -> {
                Intent intent = new Intent(StudentRoomsListActivity.this, StudentRoomMapActivity.class);
                // Optional: Animation to make it feel like a toggle
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            });
        }

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