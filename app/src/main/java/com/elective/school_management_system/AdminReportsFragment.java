package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ad_reports_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());

        recyclerView = view.findViewById(R.id.recyclerViewReports);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportList = new ArrayList<>();
        adapter = new ReportAdapter(getContext(), reportList, report -> {
            // Handle click: Open detail activity
            Intent intent = new Intent(getContext(), AdminReportDetailActivity.class);
            intent.putExtra("REPORT_ID", report.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        loadReports();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReports(); // Refresh list when returning
    }

    private void loadReports() {
        progressBar.setVisibility(View.VISIBLE);

        // Fetch from DB
        List<Report> fetched = dbHelper.getAllReports(); // Ensure this method exists in DatabaseHelper

        reportList.clear();
        if (fetched != null) {
            reportList.addAll(fetched);
        }

        progressBar.setVisibility(View.GONE);

        if (reportList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}