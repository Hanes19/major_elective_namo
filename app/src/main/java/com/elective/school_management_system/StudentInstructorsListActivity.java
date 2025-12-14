package com.elective.school_management_system;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentInstructorsListActivity extends AppCompatActivity {

    private ImageButton btnBack; // Changed to ImageButton to match typical usage
    private View btnAddInstructor; // The admin button we want to hide
    private EditText etSearch;
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private StudentInstructorAdapter adapter;
    private List<Instructor> fullInstructorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_instructors_list); // Uses the shared layout

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        // Cast to ImageButton as per typical XML, though View works too
        btnBack = findViewById(R.id.btnBack);

        // Find the Admin "Add" button and hide it
        btnAddInstructor = findViewById(R.id.btnAddInstructor);
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