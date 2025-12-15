package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherUpdatesActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private LinearLayout navMaps, navDashboard, navUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_update_list);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        navMaps = findViewById(R.id.bottomNav).findViewById(R.id.navMaps_placeholder); // You need IDs in bottomNav include
        // Since t_update_list.xml copies the bottom nav code manually (not via include),
        // we can find views by traversal or assuming you add IDs like in t_dashboard.

        // Based on t_update_list.xml provided, the LinearLayouts inside bottomNav DO NOT have IDs.
        // You must add IDs to the 3 LinearLayouts inside the bottomNav of t_update_list.xml:
        // @+id/navMaps, @+id/navDashboard, @+id/navUpdates
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Add listeners to bottom nav items similar to Dashboard to allow switching tabs
        // navDashboard.setOnClickListener(v -> startActivity(new Intent(this, TeacherDashboardActivity.class)));
        // navMaps.setOnClickListener(v -> startActivity(new Intent(this, TeacherRoomsActivity.class)));
    }
}