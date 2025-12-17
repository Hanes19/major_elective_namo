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

    // UI Components
    private LinearLayout cardRooms, cardInstructors, cardSchedule;
    private View btnSettings;

    // New variables for Map and Search
    private View mapBanner;
    private View searchContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        // Settings Button
        btnSettings = view.findViewById(R.id.img_settings);

        // Map Banner & Search Container
        mapBanner = view.findViewById(R.id.map_banner);
        searchContainer = view.findViewById(R.id.search_container);
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

        // 4. Map Banner (Fix for the issue)
        if (mapBanner != null) {
            mapBanner.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), StudentRoomMapActivity.class);
                startActivity(intent);
            });
        }

        // 5. Search Bar (Optional interaction)
        if (searchContainer != null) {
            searchContainer.setOnClickListener(v -> {
                // Redirect to Rooms List for searching
                Intent intent = new Intent(requireContext(), StudentRoomsListActivity.class);
                startActivity(intent);
            });
        }

        // Settings Button
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), NavSettingsActivity.class);
                startActivity(intent);
            });
        }
    }
}