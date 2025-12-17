package com.elective.school_management_system;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class AdminMainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout navMaps, navDashboard, navUpdates;
    private ImageView iconMaps, iconDashboard, iconUpdates;
    private TextView textMaps, textDashboard, textUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        initViews();
        setupViewPager();
        setupBottomNav();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);

        // Navigation Layouts
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);

        // Icons
        iconMaps = findViewById(R.id.iconMaps);
        iconDashboard = findViewById(R.id.iconDashboard);
        iconUpdates = findViewById(R.id.iconUpdates);

        // Text Labels
        textMaps = findViewById(R.id.textMaps);
        textDashboard = findViewById(R.id.textDashboard);
        textUpdates = findViewById(R.id.textUpdates);
    }

    private void setupViewPager() {
        AdminViewPagerAdapter adapter = new AdminViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Start at Dashboard (Index 1)
        viewPager.setCurrentItem(1, false);
        updateNavUI(1);

        // Listen for Swipe Gestures
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateNavUI(position);
            }
        });
    }

    private void setupBottomNav() {
        // Click Listeners -> Move ViewPager
        navMaps.setOnClickListener(v -> viewPager.setCurrentItem(0, true));
        navDashboard.setOnClickListener(v -> viewPager.setCurrentItem(1, true));
        navUpdates.setOnClickListener(v -> viewPager.setCurrentItem(2, true));
    }

    private void updateNavUI(int position) {
        // Colors
        int activeColor = Color.parseColor("#A9016D");
        int inactiveColor = Color.WHITE;

        // Reset All
        iconMaps.setColorFilter(inactiveColor);
        textMaps.setTextColor(inactiveColor);

        iconDashboard.setColorFilter(inactiveColor);
        textDashboard.setTextColor(inactiveColor);

        iconUpdates.setColorFilter(inactiveColor);
        textUpdates.setTextColor(inactiveColor);

        // Highlight Selected
        switch (position) {
            case 0: // Maps
                iconMaps.setColorFilter(activeColor);
                textMaps.setTextColor(activeColor);
                break;
            case 1: // Dashboard
                iconDashboard.setColorFilter(activeColor);
                textDashboard.setTextColor(activeColor);
                break;
            case 2: // Updates
                iconUpdates.setColorFilter(activeColor);
                textUpdates.setTextColor(activeColor);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // If on another tab, go back to Dashboard before exiting
        if (viewPager.getCurrentItem() != 1) {
            viewPager.setCurrentItem(1, true);
        } else {
            super.onBackPressed();
        }
    }
}