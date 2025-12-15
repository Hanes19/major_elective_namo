package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class GuestUpdatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_update_list);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Handle Bottom Nav - reuse IDs from the layout
        // Note: t_update_list.xml as provided didn't have IDs on the LinearLayouts inside bottomNav.
        // You should add IDs to them: @+id/navMaps, @+id/navDashboard, @+id/navUpdates

        // Assuming IDs are added, or finding by index:
        View bottomNav = findViewById(R.id.bottomNav);
        if (bottomNav instanceof LinearLayout) {
            LinearLayout navGroup = (LinearLayout) bottomNav;

            // Map Button
            navGroup.getChildAt(0).setOnClickListener(v -> {
                // Guests might just search for map
                startActivity(new Intent(this, StudentRoomsListActivity.class));
            });

            // Dashboard Button
            navGroup.getChildAt(1).setOnClickListener(v -> {
                Intent intent = new Intent(this, GuestDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            });

            // Updates Button (Already Here)
            navGroup.getChildAt(2).setOnClickListener(v -> {
                // Do nothing
            });
        }
    }
}