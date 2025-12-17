package com.elective.school_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StudentNavigationListFragment extends Fragment {

    private LinearLayout cardRooms, cardInstructors, cardSchedule;
    private View btnSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.s_navigation_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupListeners();
    }

    private void initViews(View view) {
        // Main Action Cards
        cardRooms = view.findViewById(R.id.card_rooms);
        cardInstructors = view.findViewById(R.id.card_instructors);
        cardSchedule = view.findViewById(R.id.card_schedule);
        btnSettings = view.findViewById(R.id.img_settings);

        // Note: Bottom Navigation views are removed as they are now in StudentMainActivity
    }

    private void setupListeners() {
        // 1. All Rooms
        cardRooms.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), StudentRoomsListActivity.class);
            startActivity(intent);
        });

        // 2. Instructors
        cardInstructors.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), StudentInstructorsListActivity.class);
            startActivity(intent);
        });

        // 3. My Schedule
        cardSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), StudentScheduleActivity.class);
            startActivity(intent);
        });

        // Settings Button
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), NavSettingsActivity.class);
                startActivity(intent);
            });
        }
    }
}