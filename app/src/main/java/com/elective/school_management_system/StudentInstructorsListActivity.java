package com.elective.school_management_system;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentInstructorsListActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etSearch;
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private StudentInstructorAdapter adapter;
    private List<Instructor> fullInstructorList; // Stores the original list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_instructors_list);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerViewInstructors);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch all data from DB
        fullInstructorList = dbHelper.getAllInstructors();

        // Initialize adapter with the full list
        adapter = new StudentInstructorAdapter(this, fullInstructorList);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        // 1. Back Button Logic
        btnBack.setOnClickListener(v -> finish());

        // 2. Search Logic
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Helper method to filter the list
    private void filter(String text) {
        List<Instructor> filteredList = new ArrayList<>();

        for (Instructor item : fullInstructorList) {
            // Check if name OR department matches the search text
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDepartment().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Update the adapter with the new filtered list
        if (adapter != null) {
            adapter.filterList(filteredList);
        }
    }
}