package com.elective.school_management_system;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout; // Import added
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ClientNavigationActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private TextureView cameraPreview;
    private Camera mCamera;
    private TextView tvNavTarget;
    private AppCompatButton btnCancelNav;
    private ImageView btnBack;

    // New Bottom Nav variables
    private LinearLayout navMaps, navDashboard, navUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_client_navscreen_armode);

        initViews();
        setupData();
        setupListeners();
    }

    private void initViews() {
        cameraPreview = findViewById(R.id.cameraPreview);
        cameraPreview.setSurfaceTextureListener(this);

        tvNavTarget = findViewById(R.id.tvNavTarget);
        btnCancelNav = findViewById(R.id.btnCancelNav);
        btnBack = findViewById(R.id.btnBack);

        // Bind new Bottom Nav IDs
        navMaps = findViewById(R.id.navMaps);
        navDashboard = findViewById(R.id.navDashboard);
        navUpdates = findViewById(R.id.navUpdates);
    }

    private void setupData() {
        if (getIntent().hasExtra("ROOM_NAME")) {
            String roomName = getIntent().getStringExtra("ROOM_NAME");
            tvNavTarget.setText("Navigating to " + roomName);
        } else {
            tvNavTarget.setText("Navigating...");
        }
    }

    private void setupListeners() {
        btnCancelNav.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());

        // 1. Maps Button -> Go to Room Map
        navMaps.setOnClickListener(v -> {
            Intent intent = new Intent(ClientNavigationActivity.this, ClientRoomMapActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        // 2. Dashboard Button -> Go Home
        navDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(ClientNavigationActivity.this, ClientDashboardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        // 3. Updates Button -> Go to Profile (or Updates Activity if you have one)
        navUpdates.setOnClickListener(v -> {
            Intent intent = new Intent(ClientNavigationActivity.this, ClientProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }

    // --- Camera Logic ---

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Camera access failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {}
}