package com.elective.school_management_system;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ReportIssueActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Spinner spinnerCategory;
    private EditText etLocation, etDescription;
    private ConstraintLayout btnUploadPhoto;
    private AppCompatButton btnSubmitReport;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupSpinner();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        etLocation = findViewById(R.id.etLocation);
        etDescription = findViewById(R.id.etDescription);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnSubmitReport = findViewById(R.id.btnSubmitReport);
    }

    private void setupSpinner() {
        // Populate the category spinner
        String[] categories = {"Maintenance", "Electrical", "Plumbing", "Cleaning", "IT/Equipment", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnUploadPhoto.setOnClickListener(v -> {
            // Placeholder for camera/gallery intent
            Toast.makeText(this, "Photo upload feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        btnSubmitReport.setOnClickListener(v -> submitReport());
    }

    private void submitReport() {
        String category = spinnerCategory.getSelectedItem().toString();
        String location = etLocation.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (location.isEmpty()) {
            etLocation.setError("Location is required");
            etLocation.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            etDescription.setError("Description is required");
            etDescription.requestFocus();
            return;
        }

        boolean isSuccess = dbHelper.addReport(location, description, category);

        if (isSuccess) {
            Toast.makeText(this, "Report submitted successfully!", Toast.LENGTH_LONG).show();
            finish(); // Close the activity and go back
        } else {
            Toast.makeText(this, "Failed to submit report. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}