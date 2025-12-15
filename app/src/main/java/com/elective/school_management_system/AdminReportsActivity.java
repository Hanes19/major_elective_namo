package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;

public class AdminReportsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private RecyclerView recyclerView;
    private Spinner spinnerFilter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_reports_list);

        dbHelper = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerReports);
        spinnerFilter = findViewById(R.id.spinnerStatusFilter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup Spinner
        String[] filters = {"All Reports", "Pending", "Resolved"};
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, filters);
        spinnerFilter.setAdapter(spinAdapter);

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReports();
    }

    private void loadReports() {
        // Simple loading without filter logic for now (or implement filter here if needed)
        List<Report> list = dbHelper.getAllReports();
        ReportAdapter adapter = new ReportAdapter(this, list, reportId -> {
            Intent intent = new Intent(this, AdminReportDetailActivity.class);
            intent.putExtra("REPORT_ID", reportId);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}