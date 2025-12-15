package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class AdminReportDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int reportId;

    private Report currentReport;
    private TextView tvRoomName, tvDate, tvDesc;
    private RadioButton rbPending, rbResolved;
    private Button btnViewOnMap;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_report_detail);

        dbHelper = new DatabaseHelper(this);
        reportId = getIntent().getIntExtra("REPORT_ID", -1);

        initViews();
        loadData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvRoomName = findViewById(R.id.tvRoomName);
        tvDate = findViewById(R.id.tvDate);
        tvDesc = findViewById(R.id.tvDescription);
        rbPending = findViewById(R.id.rbPending);
        rbResolved = findViewById(R.id.rbResolved);
        btnViewOnMap = findViewById(R.id.btnViewOnMap);

        btnBack.setOnClickListener(v -> finish());

        // Update status logic
        rbPending.setOnClickListener(v -> updateStatus("Pending"));
        rbResolved.setOnClickListener(v -> updateStatus("Resolved"));

        btnViewOnMap.setOnClickListener(v -> {
            if (currentReport != null) {
                // UPDATED: Open Admin Map
                Intent intent = new Intent(AdminReportDetailActivity.this, AdminRoomMapActivity.class);
                intent.putExtra("TARGET_ROOM", currentReport.getRoomName());
                startActivity(intent);
            }
        });

    }

    private void loadData() {
        if (reportId == -1) return;
        Report r = dbHelper.getReport(reportId);
        if (r != null) {
            tvRoomName.setText(r.getRoomName());
            tvDate.setText("Reported on " + r.getDate() + " • " + r.getCategory());
            tvDesc.setText(r.getDescription());

            if (r.getStatus().equals("Pending")) {
                rbPending.setChecked(true);
            } else {
                rbResolved.setChecked(true);
            }
        }
    }

    private void updateStatus(String status) {
        if (reportId != -1) {
            dbHelper.updateReportStatus(reportId, status);
            Toast.makeText(this, "Status updated to " + status, Toast.LENGTH_SHORT).show();
        }
    }
}