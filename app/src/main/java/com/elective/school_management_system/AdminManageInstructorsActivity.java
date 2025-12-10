package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminManageInstructorsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etSearch;
    private ImageButton fabAdd;

    // List Items
    private LinearLayout itemInstructor1, itemInstructor2;
    private ImageView btnEdit1, btnDelete1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_m_instructors);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etSearch = findViewById(R.id.etSearch);
        fabAdd = findViewById(R.id.fabAdd);

        // Items
        itemInstructor1 = findViewById(R.id.item_instructor_1);
        itemInstructor2 = findViewById(R.id.item_instructor_2);

        // Inner buttons for Item 1 (Example)
        btnEdit1 = findViewById(R.id.btnEdit1);
        btnDelete1 = findViewById(R.id.btnDelete1);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Add New Instructor
        fabAdd.setOnClickListener(v -> {
            Toast.makeText(this, "Add Instructor Clicked", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, AdminAddInstructorActivity.class);
            // startActivity(intent);
        });

        // Search Action
        etSearch.setOnClickListener(v -> Toast.makeText(this, "Type to search...", Toast.LENGTH_SHORT).show());

        // --- List Actions ---
        itemInstructor1.setOnClickListener(v -> Toast.makeText(this, "Selected Prof B", Toast.LENGTH_SHORT).show());

        // Specific Edit/Delete Actions (Need to handle carefully since they are inside the clickable row)
        if (btnEdit1 != null) {
            btnEdit1.setOnClickListener(v -> {
                Toast.makeText(this, "Edit Prof B", Toast.LENGTH_SHORT).show();
                // Prevent row click from firing if needed, but usually Android handles the top-most view
            });
        }

        if (btnDelete1 != null) {
            btnDelete1.setOnClickListener(v -> Toast.makeText(this, "Delete Prof B", Toast.LENGTH_SHORT).show());
        }
    }
}