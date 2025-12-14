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

// IMPORTS FOR VERSION 0.10.2
import io.github.sceneview.ar.ArSceneView;
import io.github.sceneview.ar.node.ArModelNode;
import io.github.sceneview.ar.node.PlacementMode;
import dev.romainguy.kotlin.math.Float3;
import kotlin.Unit;

public class ClientNavigationActivity extends AppCompatActivity implements SensorEventListener {

    private ArSceneView arSceneView;
    private ArModelNode arrowNode;
    private TextView tvNavTarget;
    private AppCompatButton btnCancelNav;
    private ImageView btnBack;
    private LinearLayout navMaps, navDashboard, navUpdates;

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
            startActivity(new Intent(ClientNavigationActivity.this, ClientRoomMapActivity.class));
            finish();
        });
        navDashboard.setOnClickListener(v -> {
            startActivity(new Intent(ClientNavigationActivity.this, ClientDashboardActivity.class));
            finish();
        });
        navUpdates.setOnClickListener(v -> {
            startActivity(new Intent(ClientNavigationActivity.this, ClientProfileActivity.class));
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
        // v0.10.2 Syntax
        arrowNode = new ArModelNode(PlacementMode.BEST_AVAILABLE);

        arrowNode.loadModelGlbAsync(
                "arrow.glb",
                true,
                0.5f,
                null,
                (throwable) -> {
                    Toast.makeText(this, "Error loading arrow", Toast.LENGTH_SHORT).show();
                    return Unit.INSTANCE;
                },
                (instance) -> {
                    return Unit.INSTANCE;
                }
        );

        arrowNode.setPosition(new Float3(0.0f, -0.5f, -2.0f));
        arSceneView.addChild(arrowNode);
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
                float azimuthInDegrees = (float) (Math.toDegrees(orientation[0]) + 360) % 360;

                if (arrowNode != null) {
                    // v0.10.2 Syntax
                    arrowNode.setRotation(new Float3(0.0f, -azimuthInDegrees, 0.0f));
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}