package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminReportsActivity extends AppCompatActivity {

    private ImageButton btnBack, btnAddReport;
    private RecyclerView recyclerView;
    private Spinner spinnerFilter;
    private DatabaseHelper dbHelper;
    private LinearLayout navMaps, navDashboard, navUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_reports_list);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup Spinner
        String[] filters = {"All Reports", "Pending", "Resolved"};
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, filters);
        spinnerFilter.setAdapter(spinAdapter);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnAddReport = findViewById(R.id.btnAddReport);
        recyclerView = findViewById(R.id.recyclerReports);
        spinnerFilter = findViewById(R.id.spinnerStatusFilter);

        // Bottom Navigation
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        // Highlight current tab (Updates)
        navUpdates.setAlpha(1.0f);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Navigation Logic matches AdminDashboard
        navMaps.setOnClickListener(v -> {
            Intent intent = new Intent(AdminReportsActivity.this, StudentRoomMapActivity.class);
            startActivity(intent);
            finish();
        });

        navDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminReportsActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish();
        });

        // navUpdates is already current activity

        // Add Report Logic
        btnAddReport.setOnClickListener(v -> {
            Intent intent = new Intent(AdminReportsActivity.this, ReportIssueActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReports();
    }

    private void loadReports() {
        List<Report> list = dbHelper.getAllReports();
        ReportAdapter adapter = new ReportAdapter(this, list, reportId -> {
            Intent intent = new Intent(this, AdminReportDetailActivity.class);
            intent.putExtra("REPORT_ID", reportId);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}