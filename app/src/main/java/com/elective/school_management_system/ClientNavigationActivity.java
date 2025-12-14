package com.elective.school_management_system;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import io.github.sceneview.ar.ArSceneView;
import io.github.sceneview.ar.node.ArModelNode;
import io.github.sceneview.ar.node.PlacementMode;
import io.github.sceneview.math.Position;
import io.github.sceneview.math.Rotation;

public class ClientNavigationActivity extends AppCompatActivity implements SensorEventListener {

    private ArSceneView arSceneView;
    private ArModelNode arrowNode;

    private TextView tvNavTarget;
    private AppCompatButton btnCancelNav;
    private ImageView btnBack;
    private LinearLayout navMaps, navDashboard, navUpdates;

    // Compass / Sensor Variables
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean lastAccelerometerSet = false;
    private boolean lastMagnetometerSet = false;
    private float[] r = new float[9];
    private float[] orientation = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_client_navscreen_armode);

        initViews();
        setupData();
        setupListeners();
        setupSensors();
        setupAR();
    }

    private void initViews() {
        arSceneView = findViewById(R.id.arSceneView);
        tvNavTarget = findViewById(R.id.tvNavTarget);
        btnCancelNav = findViewById(R.id.btnCancelNav);
        btnBack = findViewById(R.id.btnBack);

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

        navMaps.setOnClickListener(v -> {
            Intent intent = new Intent(ClientNavigationActivity.this, ClientRoomMapActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        navDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(ClientNavigationActivity.this, ClientDashboardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        navUpdates.setOnClickListener(v -> {
            Intent intent = new Intent(ClientNavigationActivity.this, ClientProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }

    private void setupSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
    }

    private void setupAR() {
        // Initialize the AR Arrow Node
        // PlacementMode.BEST_AVAILABLE means it stays stable in the world
        arrowNode = new ArModelNode(arSceneView.getEngine(), PlacementMode.BEST_AVAILABLE);

        // Load the 3D model "arrow.glb" from assets
        arrowNode.loadModelGlbAsync(
                this,
                "arrow.glb", // MAKE SURE YOU HAVE THIS FILE IN src/main/assets/
                true,
                true,
                (renderable, throwable) -> {
                    if (throwable != null) {
                        Toast.makeText(this, "Failed to load arrow model", Toast.LENGTH_SHORT).show();
                    }
                    return null;
                }
        );

        // Position the arrow 1.5 meters in front of the camera and slightly down
        // x=0 (center), y=-0.5 (slightly down), z=-1.5 (forward)
        arrowNode.setPosition(new Position(0.0f, -0.5f, -1.5f));

        // Add to the scene
        arSceneView.getScene().addChild(arrowNode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ArSceneView handles its own cleanup
    }

    // --- Compass Logic ---

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            lastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            lastMagnetometerSet = true;
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            if (SensorManager.getRotationMatrix(r, null, lastAccelerometer, lastMagnetometer)) {
                SensorManager.getOrientation(r, orientation);

                // Convert azimuth (radians) to degrees
                float azimuthInDegrees = (float) (Math.toDegrees(orientation[0]) + 360) % 360;

                // Update Arrow Rotation if the node is loaded
                if (arrowNode != null) {
                    // We rotate the arrow around the Y-axis to point North (or towards target)
                    // Note: This points 'North'. If you have a target bearing, subtract it here.
                    // Rotation(x, y, z) in degrees
                    arrowNode.setRotation(new Rotation(0, -azimuthInDegrees, 0));
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}