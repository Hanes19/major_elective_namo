package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        highlightCurrentTab();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);
    }

    private void highlightCurrentTab() {
        // visually highlight "Updates" since we are in TeacherUpdatesActivity
        if (navUpdates != null) {
            navUpdates.setAlpha(1.0f); // Make it fully opaque
            // Optional: Tint icon if you want extra polish
            // ((ImageView)navUpdates.getChildAt(0)).setColorFilter(Color.parseColor("#A9016D"));
        }
    }

    private void setupListeners() {
        // Back Button: Reverse Animation (Slide Left)
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Navigation Logic

        // Go Left to Dashboard
        navDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Go Far Left to Maps
        navMaps.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherRoomsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Already on Updates
        navUpdates.setOnClickListener(v -> {});
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}