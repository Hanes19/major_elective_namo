package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminManageInstructorsActivity extends AppCompatActivity {

    private ImageView btnBack; // Note: In your XML this is ImageButton, but casting to ImageView is fine.
    private EditText etSearch;
    private ImageButton fabAdd;
    private RecyclerView recyclerView;
    private InstructorAdapter adapter;
    private DatabaseHelper dbHelper;

    // Added Navigation Views
    private LinearLayout navMaps, navDashboard, navUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_manage_instructors);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etSearch = findViewById(R.id.etSearch);
        fabAdd = findViewById(R.id.fabAdd);
        recyclerView = findViewById(R.id.recyclerViewInstructors);

        // Added initialization for Bottom Navigation
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);
    }

    private void setupRecyclerView() {
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            loadInstructors();
        }
    }

    private void loadInstructors() {
        List<Instructor> list = dbHelper.getAllInstructors();
        adapter = new InstructorAdapter(this, list, dbHelper);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        fabAdd.setOnClickListener(v -> showAddInstructorDialog());
        etSearch.setOnClickListener(v -> Toast.makeText(this, "Search feature coming soon...", Toast.LENGTH_SHORT).show());

        // Added Navigation Logic
        if (navMaps != null) {
            navMaps.setOnClickListener(v -> {
                Intent intent = new Intent(AdminManageInstructorsActivity.this, AdminMapsFragment.class);
                startActivity(intent);
            });
        }

        if (navDashboard != null) {
            navDashboard.setOnClickListener(v -> {
                Intent intent = new Intent(AdminManageInstructorsActivity.this, AdminDashboardFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        if (navUpdates != null) {
            navUpdates.setOnClickListener(v -> {
                Intent intent = new Intent(AdminManageInstructorsActivity.this, AdminReportsFragment.class);
                startActivity(intent);
            });
        }
    }

    private void showAddInstructorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Instructor");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        final EditText etName = new EditText(this);
        etName.setHint("Instructor Name");
        layout.addView(etName);

        final EditText etDept = new EditText(this);
        etDept.setHint("Department");
        layout.addView(etDept);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = etName.getText().toString();
            String dept = etDept.getText().toString();
            if (!name.isEmpty()) {
                boolean success = dbHelper.addInstructor(name, dept);
                if (success) {
                    Toast.makeText(this, "Instructor Added!", Toast.LENGTH_SHORT).show();
                    loadInstructors(); // Refresh list
                } else {
                    Toast.makeText(this, "Error adding instructor", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}