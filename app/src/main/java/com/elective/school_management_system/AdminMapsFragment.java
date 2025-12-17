package com.elective.school_management_system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminMapsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Important: Make sure to remove the bottom nav from 'ad_room_map.xml' if it exists there,
        // as the Activity now handles navigation.
        return inflater.inflate(R.layout.ad_room_map, container, false);
    }
}