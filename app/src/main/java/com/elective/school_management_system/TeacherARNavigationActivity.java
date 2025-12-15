package com.elective.school_management_system;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class TeacherARNavigationActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private AppCompatButton btnCancelNav;
    private TextView tvNavTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_navscreen_armode);

        btnBack = findViewById(R.id.btnBack);
        btnCancelNav = findViewById(R.id.btnCancelNav);
        tvNavTarget = findViewById(R.id.tvNavTarget);

        String roomName = getIntent().getStringExtra("TARGET_ROOM");
        if(roomName != null) {
            tvNavTarget.setText("Navigating to " + roomName);
        }

        btnBack.setOnClickListener(v -> finish());
        btnCancelNav.setOnClickListener(v -> finish());
    }
}