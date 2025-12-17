package com.elective.school_management_system;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class StudentMainActivity extends AppCompatActivity {

    private LinearLayout navHome, navNav, navProfile;
    private ImageView iconHome, iconNav, iconProfile;
    private TextView textHome, textNav, textProfile;

    // Track which tab is currently selected (0=Home, 1=Nav, 2=Profile)
    private int currentTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        initViews();
        setupListeners();

        // Load default fragment (Dashboard)
        loadFragment(new StudentDashboardFragment(), 0);
    }

    private void initViews() {
        navHome = findViewById(R.id.navHome);
        navNav = findViewById(R.id.navNav);
        navProfile = findViewById(R.id.navProfile);

        iconHome = findViewById(R.id.iconHome);
        iconNav = findViewById(R.id.iconNav);
        iconProfile = findViewById(R.id.iconProfile);

        textHome = findViewById(R.id.textHome);
        textNav = findViewById(R.id.textNav);
        textProfile = findViewById(R.id.textProfile);
    }

    private void setupListeners() {
        navHome.setOnClickListener(v -> loadFragment(new StudentDashboardFragment(), 0));
        navNav.setOnClickListener(v -> loadFragment(new StudentNavigationListFragment(), 1));
        navProfile.setOnClickListener(v -> loadFragment(new StudentProfileFragment(), 2));
    }

    private void loadFragment(Fragment fragment, int newTab) {
        if (newTab == currentTab && getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
            return; // Don't reload if already on the tab
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // ANIMATION LOGIC:
        // If going right (e.g., Home -> Nav), animate In Right / Out Left
        // If going left (e.g., Profile -> Home), animate In Left / Out Right
        if (newTab > currentTab) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        currentTab = newTab;
        updateNavUI(newTab);
    }

    private void updateNavUI(int activeTab) {
        // Reset all to inactive (White/Regular)
        int inactiveColor = Color.WHITE;
        int activeColor = Color.parseColor("#A9016D");

        iconHome.setColorFilter(inactiveColor);
        iconNav.setColorFilter(inactiveColor);
        iconProfile.setColorFilter(inactiveColor);

        // Highlight Active
        if (activeTab == 0) {
            iconHome.setColorFilter(activeColor);
            textHome.setTextColor(activeColor);
            textNav.setTextColor(inactiveColor);
            textProfile.setTextColor(inactiveColor);
        } else if (activeTab == 1) {
            iconNav.setColorFilter(activeColor);
            textNav.setTextColor(activeColor);
            textHome.setTextColor(inactiveColor);
            textProfile.setTextColor(inactiveColor);
        } else if (activeTab == 2) {
            iconProfile.setColorFilter(activeColor);
            textProfile.setTextColor(activeColor);
            textHome.setTextColor(inactiveColor);
            textNav.setTextColor(inactiveColor);
        }
    }
}