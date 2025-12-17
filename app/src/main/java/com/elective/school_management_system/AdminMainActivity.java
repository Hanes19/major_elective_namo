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

        // FIX: Handle incoming intents to switch tabs
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.hasExtra("TAB_INDEX")) {
            int tabIndex = intent.getIntExtra("TAB_INDEX", 1);
            viewPager.setCurrentItem(tabIndex, false);
            updateNavUI(tabIndex);
        }
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);

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

    private void setupViewPager() {
        AdminViewPagerAdapter adapter = new AdminViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Start at Dashboard (Index 1) by default
        viewPager.setCurrentItem(1, false);
        updateNavUI(1);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateNavUI(position);
            }
        });
    }

    private void setupBottomNav() {
        navMaps.setOnClickListener(v -> viewPager.setCurrentItem(0, true));
        navDashboard.setOnClickListener(v -> viewPager.setCurrentItem(1, true));
        navUpdates.setOnClickListener(v -> viewPager.setCurrentItem(2, true));
    }

    private void updateNavUI(int position) {
        int activeColor = Color.parseColor("#A9016D");
        int inactiveColor = Color.WHITE;

        iconMaps.setColorFilter(inactiveColor);
        textMaps.setTextColor(inactiveColor);

        iconDashboard.setColorFilter(inactiveColor);
        textDashboard.setTextColor(inactiveColor);

        iconUpdates.setColorFilter(inactiveColor);
        textUpdates.setTextColor(inactiveColor);

        switch (position) {
            case 0:
                iconMaps.setColorFilter(activeColor);
                textMaps.setTextColor(activeColor);
                break;
            case 1:
                iconDashboard.setColorFilter(activeColor);
                textDashboard.setTextColor(activeColor);
                break;
            case 2:
                iconUpdates.setColorFilter(activeColor);
                textUpdates.setTextColor(activeColor);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 1) {
            viewPager.setCurrentItem(1, true);
        } else {
            super.onBackPressed();
        }
    }
}