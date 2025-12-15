package com.elective.school_management_system;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentInstructorsListActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private View btnAddInstructor;
    private EditText etSearch;
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private StudentInstructorAdapter adapter;
    private List<Instructor> fullInstructorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_manage_instructors);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupRecyclerView();
        setupListeners();

        // NEW: Handle search query passed from Dashboard
        if (getIntent().hasExtra("SEARCH_QUERY")) {
            String query = getIntent().getStringExtra("SEARCH_QUERY");
            if (etSearch != null) {
                etSearch.setText(query);
                // Trigger filter manually just in case TextWatcher doesn't catch the set text immediately
                filter(query);
            }
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnAddInstructor = findViewById(R.id.btnAddInstructor);

        // Hide Admin Button
        if (btnAddInstructor != null) {
            btnAddInstructor.setVisibility(View.GONE);
        }

        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerViewInstructors);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fullInstructorList = dbHelper.getAllInstructors();
        adapter = new StudentInstructorAdapter(this, fullInstructorList);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

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

    private void filter(String text) {
        List<Instructor> filteredList = new ArrayList<>();
        for (Instructor item : fullInstructorList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDepartment().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (adapter != null) {
            adapter.filterList(filteredList);
        }
    }
}