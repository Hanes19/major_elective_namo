package com.elective.school_management_system;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class AdminDashboardFragment extends Fragment {

    private ConstraintLayout btnManageMap, btnManageUsers, btnReports;
    private LinearLayout cardActiveUsers, cardNewReports, cardSysHealth;
    private TextView tvActiveUsersCount, tvNewReportsCount, tvSysHealthCount;
    private FrameLayout btnSettings;
    private DatabaseHelper dbHelper;

    private int totalUsers = 0, studentCount = 0, guestCount = 0, pendingReports = 0;
    private String reportsBreakdown = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(requireContext());
        initViews(view);
        setupListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDashboardStats();
    }

    private void initViews(View view) {
        btnManageMap = view.findViewById(R.id.btnManageMap);
        btnManageUsers = view.findViewById(R.id.btnManageUsers);
        btnReports = view.findViewById(R.id.btnReports);

        cardActiveUsers = view.findViewById(R.id.cardActiveUsers);
        cardNewReports = view.findViewById(R.id.cardNewReports);
        cardSysHealth = view.findViewById(R.id.cardSysHealth);

        tvActiveUsersCount = view.findViewById(R.id.tvActiveUsersCount);
        tvNewReportsCount = view.findViewById(R.id.tvNewReportsCount);
        tvSysHealthCount = view.findViewById(R.id.tvSysHealthCount);

        btnSettings = view.findViewById(R.id.btnSettings);
    }

    private void updateDashboardStats() {
        totalUsers = dbHelper.getTotalUserCount();
        studentCount = dbHelper.getStudentCount();
        guestCount = totalUsers - studentCount;
        pendingReports = dbHelper.getPendingReportsCount();
        reportsBreakdown = dbHelper.getPendingReportBreakdown();

        tvActiveUsersCount.setText(String.valueOf(totalUsers));
        tvNewReportsCount.setText(String.valueOf(pendingReports));

        // Mock health for demo
        int health = 98 + (int)(Math.random() * 3) - 1;
        tvSysHealthCount.setText(health + "%");
    }

    private void setupListeners() {
        // These buttons open dedicated Activities
        btnManageMap.setOnClickListener(v -> startActivity(new Intent(getActivity(), AdminMapsFragment.class)));
        btnManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AdminManageUsersActivity.class);
            startActivity(intent);
        });

        btnReports.setOnClickListener(v -> startActivity(new Intent(getActivity(), AdminReportsFragment.class)));

        btnSettings.setOnClickListener(v -> startActivity(new Intent(getActivity(), NavSettingsActivity.class)));

        // [MODIFIED] Active Users card now opens the User List Activity
        cardActiveUsers.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AdminUserListFragment.class));
        });

        cardNewReports.setOnClickListener(v -> {
            String msg = "Pending Reports: " + pendingReports + "\n\nBreakdown by Category:\n" + reportsBreakdown;
            showDescriptionDialog("Report Statistics", msg);
        });

        cardSysHealth.setOnClickListener(v -> {
            String healthStats = "Overall Stability: " + tvSysHealthCount.getText() + "\n\n• Database: Online\n• Server Load: Normal";
            showDescriptionDialog("System Health Diagnostics", healthStats);
        });
    }

    private void showDescriptionDialog(String title, String message) {
        if(getContext() == null) return;
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}