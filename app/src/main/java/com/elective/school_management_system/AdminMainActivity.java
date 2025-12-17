package com.elective.school_management_system;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AdminMainActivity extends AppCompatActivity {

    private LinearLayout navMaps, navDashboard, navUpdates;
    private ImageView iconMaps, iconDashboard, iconUpdates;
    private TextView textMaps, textDashboard, textUpdates;

    // Track active tab: 0=Maps, 1=Dashboard, 2=Updates
    // Defaulting to 1 (Dashboard) as the center/home screen
    private int currentTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        initViews();
        setupListeners();

        // Load Default Fragment (Dashboard)
        loadFragment(new AdminDashboardFragment(), 1);
    }

    private void initViews() {
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        iconMaps = findViewById(R.id.iconMaps);
        iconDashboard = findViewById(R.id.iconDashboard);
        iconUpdates = findViewById(R.id.iconUpdates);

        textMaps = findViewById(R.id.textMaps);
        textDashboard = findViewById(R.id.textDashboard);
        textUpdates = findViewById(R.id.textUpdates);
    }

    private void setupListeners() {
        // Tab 0
        navMaps.setOnClickListener(v -> loadFragment(new AdminMapsFragment(), 0));

        // Tab 1
        navDashboard.setOnClickListener(v -> loadFragment(new AdminDashboardFragment(), 1));

        // Tab 2
        navUpdates.setOnClickListener(v -> loadFragment(new AdminReportsFragment(), 2));
    }

    private void loadFragment(Fragment fragment, int newTab) {
        if (newTab == currentTab && getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Animation Logic
        if (newTab > currentTab) {
            // Moving Right (e.g., Dashboard -> Updates)
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (newTab < currentTab) {
            // Moving Left (e.g., Dashboard -> Maps)
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        currentTab = newTab;
        updateNavUI(newTab);
    }

    private void updateNavUI(int activeTab) {
        int inactiveColor = Color.WHITE;
        int activeColor = Color.parseColor("#A9016D");

        // Reset all
        iconMaps.setColorFilter(inactiveColor);
        textMaps.setTextColor(inactiveColor);

        iconDashboard.setColorFilter(inactiveColor);
        textDashboard.setTextColor(inactiveColor);

        iconUpdates.setColorFilter(inactiveColor);
        textUpdates.setTextColor(inactiveColor);

        // Highlight Active
        if (activeTab == 0) {
            iconMaps.setColorFilter(activeColor);
            textMaps.setTextColor(activeColor);
        } else if (activeTab == 1) {
            iconDashboard.setColorFilter(activeColor);
            textDashboard.setTextColor(activeColor);
        } else if (activeTab == 2) {
            iconUpdates.setColorFilter(activeColor);
            textUpdates.setTextColor(activeColor);
        }
    }
}