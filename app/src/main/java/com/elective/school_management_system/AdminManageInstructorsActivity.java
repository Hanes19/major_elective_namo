package com.elective.school_management_system;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminManageInstructorsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etSearch;
    private ImageButton fabAdd;
    private DatabaseHelper dbHelper;

    // List Items (Static for now, but functionality added for "Add")
    private LinearLayout itemInstructor1;
    private ImageView btnEdit1, btnDelete1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_m_instructors);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etSearch = findViewById(R.id.etSearch);
        fabAdd = findViewById(R.id.fabAdd);
        itemInstructor1 = findViewById(R.id.item_instructor_1);
        btnEdit1 = findViewById(R.id.btnEdit1);
        btnDelete1 = findViewById(R.id.btnDelete1);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Add New Instructor Dialog
        fabAdd.setOnClickListener(v -> showAddInstructorDialog());

        etSearch.setOnClickListener(v -> Toast.makeText(this, "Type to search...", Toast.LENGTH_SHORT).show());

        if (itemInstructor1 != null) {
            itemInstructor1.setOnClickListener(v -> Toast.makeText(this, "Selected Prof B", Toast.LENGTH_SHORT).show());
        }

        if (btnEdit1 != null) {
            btnEdit1.setOnClickListener(v -> Toast.makeText(this, "Edit functionality here", Toast.LENGTH_SHORT).show());
        }

        if (btnDelete1 != null) {
            btnDelete1.setOnClickListener(v -> Toast.makeText(this, "Delete functionality here", Toast.LENGTH_SHORT).show());
        }
    }

    private void showAddInstructorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Instructor");

        final EditText input = new EditText(this);
        input.setHint("Enter Instructor Name");
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = input.getText().toString();
            if (!name.isEmpty()) {
                boolean success = dbHelper.addInstructor(name, "Department Placeholder");
                if (success) {
                    Toast.makeText(AdminManageInstructorsActivity.this, "Instructor Added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminManageInstructorsActivity.this, "Error adding instructor", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AdminManageInstructorsActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}