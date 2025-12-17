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

        // CORRECTED LINE
        recyclerView = view.findViewById(R.id.recyclerReports);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportList = new ArrayList<>();

        // FIX: The lambda receives an 'int' (reportId), not a 'Report' object.
        adapter = new ReportAdapter(getContext(), reportList, reportId -> {
            Intent intent = new Intent(getContext(), AdminReportDetailActivity.class);
            intent.putExtra("REPORT_ID", reportId); // Use reportId directly
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        loadReports();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReports();
    }

    private void loadReports() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        List<Report> fetched = dbHelper.getAllReports();

        reportList.clear();
        if (fetched != null) {
            reportList.addAll(fetched);
        }

        if (progressBar != null) progressBar.setVisibility(View.GONE);

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