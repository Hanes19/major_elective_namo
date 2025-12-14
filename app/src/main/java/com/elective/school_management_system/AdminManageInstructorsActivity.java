package com.elective.school_management_system;

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

    private ImageView btnBack;
    private EditText etSearch;
    private ImageButton fabAdd;
    private RecyclerView recyclerView;
    private InstructorAdapter adapter;
    private DatabaseHelper dbHelper;

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
        // Ensure you added this ID to your XML file!
        recyclerView = findViewById(R.id.recyclerViewInstructors);
    }

    private void setupRecyclerView() {
        // If the RecyclerView is null, it means the XML hasn't been updated yet.
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