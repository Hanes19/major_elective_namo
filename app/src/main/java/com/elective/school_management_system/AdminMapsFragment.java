package com.elective.school_management_system;

import android.content.Intent; // Import Intent
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView; // Import TextView
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AdminMapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView tabList; // Changed from ImageButton to TextView to match XML
    private ImageButton btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // This inflates ad_room_map.xml
        return inflater.inflate(R.layout.ad_room_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize Back Button
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // FIX: Use 'tabList' to navigate to the Rooms List
        tabList = view.findViewById(R.id.tabList);
        tabList.setOnClickListener(v -> {
            // Check if context is available
            if (getContext() != null) {
                // Assuming you have an Activity for the list, e.g., AdminManageRoomsActivity
                // If you are using fragments, use FragmentManager here instead.

                // Example: Navigating to the Activity that uses s_rooms_list.xml (e.g., StudentRoomsListActivity)
                // Note: Ensure you use the correct Activity class name here.
                Intent intent = new Intent(getContext(), StudentRoomsListActivity.class);
                startActivity(intent);

                // Optional: Finish current activity if you don't want to come back
                // if (getActivity() != null) getActivity().finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Example Campus Coordinates
        LatLng campusLocation = new LatLng(8.4859, 124.6567);
        mMap.addMarker(new MarkerOptions().position(campusLocation).title("Campus Main"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campusLocation, 18f));

        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}