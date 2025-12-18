package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminReportsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private List<Report> reportList;
    private DatabaseHelper dbHelper;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private Spinner spinnerFilter; // Added filter view

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ad_reports_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());

        recyclerView = view.findViewById(R.id.recyclerReports);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        spinnerFilter = view.findViewById(R.id.spinnerStatusFilter); // Initialize Spinner

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportList = new ArrayList<>();

        adapter = new ReportAdapter(requireContext(), reportList, reportId -> {
            Intent intent = new Intent(getContext(), AdminReportDetailActivity.class);
            intent.putExtra("REPORT_ID", reportId);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Listener for the Status Filter
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadReports();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        loadReports();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReports();
    }

    private void loadReports() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        // Fetch all reports from the database
        List<Report> allFetched = dbHelper.getAllReports();
        String selectedStatus = spinnerFilter.getSelectedItem().toString();

        reportList.clear();
        if (allFetched != null) {
            for (Report r : allFetched) {
                // Logic: Only show Maintenance category and match the status
                boolean isMaintenance = r.getCategory().equalsIgnoreCase("Maintenance");
                boolean matchesStatus = selectedStatus.equalsIgnoreCase("All") ||
                        r.getStatus().equalsIgnoreCase(selectedStatus);

                if (isMaintenance && matchesStatus) {
                    reportList.add(r);
                }
            }
        }

        if (progressBar != null) progressBar.setVisibility(View.GONE);

        // Handle Empty State Visibility
        if (reportList.isEmpty()) {
            if (tvEmptyState != null) tvEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            if (tvEmptyState != null) tvEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}