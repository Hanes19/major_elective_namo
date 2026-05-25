package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class GuestTutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_tutorial);

        // 1. Back Button
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
            // Optional: Slide out animation to match entry
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        // 2. "Find Admissions Office" Button (CTA)
        // This reuses the AR Navigation logic from the dashboard
        Button btnStartApplication = findViewById(R.id.btnStartApplication);
        btnStartApplication.setOnClickListener(v -> {
            Intent intent = new Intent(GuestTutorialActivity.this, TeacherARNavigationActivity.class);
            intent.putExtra("TARGET_ROOM", "Admissions");
            startActivity(intent);
        });
    }
}